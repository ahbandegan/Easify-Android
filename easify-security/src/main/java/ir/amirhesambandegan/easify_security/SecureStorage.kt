package ir.amirhesambandegan.easify_security

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.DeterministicAead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.daead.DeterministicAeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import org.json.JSONArray
import java.nio.ByteBuffer
import java.util.concurrent.CopyOnWriteArraySet

/**
 * Modern replacement for the deprecated Jetpack `androidx.security:security-crypto` library,
 * powered directly by **Google Tink** and Android Keystore hardware-backed master keys.
 *
 * Uses AES-256-SIV ([DeterministicAead]) for preference keys (preserving deterministic lookups)
 * and AES-256-GCM ([Aead]) with key-bound Associated Authenticated Data (AAD) for values.
 */
object SecureStorage {

    private const val MASTER_KEY_URI = "android-keystore://easify_tink_master_key"

    @Volatile
    private var tinkRegistered = false
    private val initLock = Any()
    private val cachedPrefs = mutableMapOf<String, TinkEncryptedSharedPreferences>()

    private fun ensureTinkRegistered() {
        if (!tinkRegistered) {
            synchronized(initLock) {
                if (!tinkRegistered) {
                    AeadConfig.register()
                    DeterministicAeadConfig.register()
                    tinkRegistered = true
                }
            }
        }
    }

    /**
     * Returns an encrypted [SharedPreferences] instance backed by Google Tink AES-256-SIV (keys)
     * and AES-256-GCM (values), sealed with an Android Keystore master key.
     *
     * @param context Application or UI context (converted internally to `applicationContext`).
     * @param fileName Name of the backing SharedPreferences file. Defaults to `"secure_prefs"`.
     */
    fun getEncryptedPreferences(
        context: Context,
        fileName: String = "secure_prefs"
    ): SharedPreferences {
        val safeName = fileName.trim().ifEmpty { "secure_prefs" }
        synchronized(cachedPrefs) {
            cachedPrefs[safeName]?.let { return it }

            ensureTinkRegistered()
            val appContext = context.applicationContext

            val valueKeysetHandle = AndroidKeysetManager.Builder()
                .withSharedPref(appContext, "__easify_tink_val_keyset_${safeName}__", safeName)
                .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()
                .keysetHandle

            val keyKeysetHandle = AndroidKeysetManager.Builder()
                .withSharedPref(appContext, "__easify_tink_key_keyset_${safeName}__", safeName)
                .withKeyTemplate(KeyTemplates.get("AES256_SIV"))
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()
                .keysetHandle

            val valueAead = valueKeysetHandle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)
            val keyDaead = keyKeysetHandle.getPrimitive(
                RegistryConfiguration.get(),
                DeterministicAead::class.java
            )

            val delegate = appContext.getSharedPreferences(safeName, Context.MODE_PRIVATE)
            return TinkEncryptedSharedPreferences(
                fileName = safeName,
                delegate = delegate,
                keyDaead = keyDaead,
                valueAead = valueAead
            ).also { cachedPrefs[safeName] = it }
        }
    }

    /**
     * Returns a Google Tink [Aead] primitive bound to the given [keysetAlias] and protected
     * by Android Keystore for arbitrary authenticated encryption/decryption.
     */
    fun getAead(context: Context, keysetAlias: String = "easify_default_aead"): Aead {
        ensureTinkRegistered()
        val appContext = context.applicationContext
        val handle = AndroidKeysetManager.Builder()
            .withSharedPref(appContext, "__easify_tink_aead_${keysetAlias}__", "easify_tink_keysets")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
        return handle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)
    }
}

internal class TinkEncryptedSharedPreferences(
    private val fileName: String,
    private val delegate: SharedPreferences,
    private val keyDaead: DeterministicAead,
    private val valueAead: Aead
) : SharedPreferences {

    private val reservedPrefix = "__easify_tink_"
    private val listeners = CopyOnWriteArraySet<SharedPreferences.OnSharedPreferenceChangeListener>()
    private val ioLock = Any()

    private fun isReservedKey(rawKey: String?): Boolean =
        rawKey == null || rawKey.startsWith(reservedPrefix)

    private fun encryptKey(plainKey: String): String {
        val bytes = keyDaead.encryptDeterministically(
            plainKey.toByteArray(Charsets.UTF_8),
            fileName.toByteArray(Charsets.UTF_8)
        )
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun decryptKey(encryptedKey: String): String? {
        if (isReservedKey(encryptedKey)) return null
        return try {
            val cipher = Base64.decode(encryptedKey, Base64.NO_WRAP)
            val plain = keyDaead.decryptDeterministically(cipher, fileName.toByteArray(Charsets.UTF_8))
            String(plain, Charsets.UTF_8)
        } catch (_: Exception) {
            null
        }
    }

    private fun encryptTypedValue(encryptedKey: String, typeTag: Byte, payload: ByteArray): String {
        val raw = ByteArray(1 + payload.size)
        raw[0] = typeTag
        payload.copyInto(raw, destinationOffset = 1)
        val encrypted = valueAead.encrypt(raw, encryptedKey.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    private fun decryptTypedValue(encryptedKey: String, expectedTag: Byte): ByteArray? {
        val encoded = delegate.getString(encryptedKey, null) ?: return null
        return try {
            val cipher = Base64.decode(encoded, Base64.NO_WRAP)
            val plain = valueAead.decrypt(cipher, encryptedKey.toByteArray(Charsets.UTF_8))
            if (plain.isEmpty() || plain[0] != expectedTag) null
            else plain.copyOfRange(1, plain.size)
        } catch (_: Exception) {
            null
        }
    }

    private fun decryptAnyValue(encryptedKey: String): Any? {
        val encoded = delegate.getString(encryptedKey, null) ?: return null
        return try {
            val cipher = Base64.decode(encoded, Base64.NO_WRAP)
            val plain = valueAead.decrypt(cipher, encryptedKey.toByteArray(Charsets.UTF_8))
            if (plain.isEmpty()) return null
            val payload = plain.copyOfRange(1, plain.size)
            when (plain[0]) {
                TYPE_NULL -> null
                TYPE_STRING -> String(payload, Charsets.UTF_8)
                TYPE_STRING_SET -> decodeStringSet(payload)
                TYPE_INT -> ByteBuffer.wrap(payload).int
                TYPE_LONG -> ByteBuffer.wrap(payload).long
                TYPE_FLOAT -> ByteBuffer.wrap(payload).float
                TYPE_BOOLEAN -> payload.isNotEmpty() && payload[0].toInt() != 0
                else -> null
            }
        } catch (_: Exception) {
            null
        }
    }

    override fun getAll(): MutableMap<String, *> {
        synchronized(ioLock) {
            val result = mutableMapOf<String, Any?>()
            for ((encKey, _) in delegate.all) {
                if (isReservedKey(encKey)) continue
                val plainKey = decryptKey(encKey) ?: continue
                result[plainKey] = decryptAnyValue(encKey)
            }
            return result
        }
    }

    override fun getString(key: String, defValue: String?): String? = synchronized(ioLock) {
        val encKey = encryptKey(key)
        val raw = decryptTypedValue(encKey, TYPE_STRING) ?: return@synchronized defValue
        String(raw, Charsets.UTF_8)
    }

    override fun getStringSet(key: String, defValues: MutableSet<String>?): MutableSet<String>? =
        synchronized(ioLock) {
            val encKey = encryptKey(key)
            val raw = decryptTypedValue(encKey, TYPE_STRING_SET) ?: return@synchronized defValues
            decodeStringSet(raw).toMutableSet()
        }

    override fun getInt(key: String, defValue: Int): Int = synchronized(ioLock) {
        val encKey = encryptKey(key)
        val raw = decryptTypedValue(encKey, TYPE_INT) ?: return@synchronized defValue
        if (raw.size != Int.SIZE_BYTES) defValue else ByteBuffer.wrap(raw).int
    }

    override fun getLong(key: String, defValue: Long): Long = synchronized(ioLock) {
        val encKey = encryptKey(key)
        val raw = decryptTypedValue(encKey, TYPE_LONG) ?: return@synchronized defValue
        if (raw.size != Long.SIZE_BYTES) defValue else ByteBuffer.wrap(raw).long
    }

    override fun getFloat(key: String, defValue: Float): Float = synchronized(ioLock) {
        val encKey = encryptKey(key)
        val raw = decryptTypedValue(encKey, TYPE_FLOAT) ?: return@synchronized defValue
        if (raw.size != Float.SIZE_BYTES) defValue else ByteBuffer.wrap(raw).float
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean = synchronized(ioLock) {
        val encKey = encryptKey(key)
        val raw = decryptTypedValue(encKey, TYPE_BOOLEAN) ?: return@synchronized defValue
        if (raw.isEmpty()) defValue else raw[0].toInt() != 0
    }

    override fun contains(key: String): Boolean = synchronized(ioLock) {
        delegate.contains(encryptKey(key))
    }

    override fun edit(): SharedPreferences.Editor = EditorImpl()

    override fun registerOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    ) {
        listeners.add(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    ) {
        listeners.remove(listener)
    }

    private inner class EditorImpl : SharedPreferences.Editor {
        private val pendingPuts = mutableMapOf<String, Pair<Byte, ByteArray>?>()
        private val pendingRemoves = mutableSetOf<String>()
        private var clearRequested = false

        override fun putString(key: String, value: String?): SharedPreferences.Editor = apply {
            if (value == null) remove(key)
            else {
                pendingRemoves.remove(key)
                pendingPuts[key] = TYPE_STRING to value.toByteArray(Charsets.UTF_8)
            }
        }

        override fun putStringSet(key: String, values: MutableSet<String>?): SharedPreferences.Editor =
            apply {
                if (values == null) remove(key)
                else {
                    pendingRemoves.remove(key)
                    val arr = JSONArray()
                    values.forEach { arr.put(it) }
                    pendingPuts[key] = TYPE_STRING_SET to arr.toString().toByteArray(Charsets.UTF_8)
                }
            }

        override fun putInt(key: String, value: Int): SharedPreferences.Editor = apply {
            pendingRemoves.remove(key)
            val bytes = ByteBuffer.allocate(Int.SIZE_BYTES).putInt(value).array()
            pendingPuts[key] = TYPE_INT to bytes
        }

        override fun putLong(key: String, value: Long): SharedPreferences.Editor = apply {
            pendingRemoves.remove(key)
            val bytes = ByteBuffer.allocate(Long.SIZE_BYTES).putLong(value).array()
            pendingPuts[key] = TYPE_LONG to bytes
        }

        override fun putFloat(key: String, value: Float): SharedPreferences.Editor = apply {
            pendingRemoves.remove(key)
            val bytes = ByteBuffer.allocate(Float.SIZE_BYTES).putFloat(value).array()
            pendingPuts[key] = TYPE_FLOAT to bytes
        }

        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor = apply {
            pendingRemoves.remove(key)
            val byteVal = byteArrayOf(if (value) 1 else 0)
            pendingPuts[key] = TYPE_BOOLEAN to byteVal
        }

        override fun remove(key: String): SharedPreferences.Editor = apply {
            pendingPuts.remove(key)
            pendingRemoves.add(key)
        }

        override fun clear(): SharedPreferences.Editor = apply {
            clearRequested = true
            pendingPuts.clear()
            pendingRemoves.clear()
        }

        override fun commit(): Boolean = persistChanges(synchronous = true)

        override fun apply() {
            persistChanges(synchronous = false)
        }

        private fun persistChanges(synchronous: Boolean): Boolean {
            val changedKeys = mutableListOf<String?>()
            val committed: Boolean

            synchronized(ioLock) {
                val editor = delegate.edit()
                if (clearRequested) {
                    for ((rawKey, _) in delegate.all) {
                        if (!isReservedKey(rawKey)) {
                            editor.remove(rawKey)
                            changedKeys.add(decryptKey(rawKey))
                        }
                    }
                }
                for (plainKey in pendingRemoves) {
                    val encKey = encryptKey(plainKey)
                    if (delegate.contains(encKey)) {
                        editor.remove(encKey)
                        changedKeys.add(plainKey)
                    }
                }
                for ((plainKey, typed) in pendingPuts) {
                    if (typed != null) {
                        val encKey = encryptKey(plainKey)
                        val encValue = encryptTypedValue(encKey, typed.first, typed.second)
                        editor.putString(encKey, encValue)
                        changedKeys.add(plainKey)
                    }
                }
                committed = if (synchronous) editor.commit() else {
                    editor.apply()
                    true
                }
            }

            if (committed && changedKeys.isNotEmpty()) {
                for (listener in listeners) {
                    for (k in changedKeys) {
                        listener.onSharedPreferenceChanged(this@TinkEncryptedSharedPreferences, k)
                    }
                }
            }
            return committed
        }
    }

    private fun decodeStringSet(bytes: ByteArray): Set<String> {
        val arr = JSONArray(String(bytes, Charsets.UTF_8))
        val set = LinkedHashSet<String>(arr.length())
        for (i in 0 until arr.length()) {
            set.add(arr.optString(i))
        }
        return set
    }

    private companion object {
        const val TYPE_NULL: Byte = 0
        const val TYPE_STRING: Byte = 1
        const val TYPE_STRING_SET: Byte = 2
        const val TYPE_INT: Byte = 3
        const val TYPE_LONG: Byte = 4
        const val TYPE_FLOAT: Byte = 5
        const val TYPE_BOOLEAN: Byte = 6
    }
}
