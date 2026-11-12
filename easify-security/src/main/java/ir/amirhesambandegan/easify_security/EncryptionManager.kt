package ir.amirhesambandegan.easify_security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Handles AES-256-GCM hardware-backed Keystore encryption/decryption along with
 * SHA-256 hashing, HMAC-SHA256 authentication, constant-time equality, and CSPRNG token generation.
 */
object EncryptionManager {

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val IV_SIZE = 12
    private const val TAG_BIT_LENGTH = 128

    private val keyLock = Any()
    private val secureRandom = SecureRandom()

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
    }

    /**
     * Encrypts [plainText] using the Android Keystore key for [alias] and returns a Base64-encoded IV + ciphertext string.
     */
    fun encrypt(alias: String, plainText: String): String {
        val encrypted = encryptBytes(alias, plainText.toByteArray(Charsets.UTF_8)) ?: return ""
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    /**
     * Decrypts a Base64-encoded payload produced by [encrypt], or returns `null` if authentication/decryption fails.
     */
    fun decrypt(alias: String, encryptedText: String): String? {
        if (encryptedText.isBlank()) return null
        return try {
            val combined = Base64.decode(encryptedText, Base64.DEFAULT)
            val plain = decryptBytes(alias, combined) ?: return null
            String(plain, Charsets.UTF_8)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Encrypts raw [data] using AES-256-GCM under [alias], prepending the 12-byte random IV.
     */
    fun encryptBytes(alias: String, data: ByteArray): ByteArray? {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey(alias))
            val iv = cipher.iv
            val cipherBytes = cipher.doFinal(data)
            ByteArray(iv.size + cipherBytes.size).also { combined ->
                iv.copyInto(combined, destinationOffset = 0)
                cipherBytes.copyInto(combined, destinationOffset = iv.size)
            }
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Decrypts raw IV + ciphertext bytes produced by [encryptBytes].
     */
    fun decryptBytes(alias: String, combined: ByteArray): ByteArray? {
        if (combined.size <= IV_SIZE) return null
        return try {
            val iv = combined.copyOfRange(0, IV_SIZE)
            val cipherBytes = combined.copyOfRange(IV_SIZE, combined.size)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(alias), GCMParameterSpec(TAG_BIT_LENGTH, iv))
            cipher.doFinal(cipherBytes)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Deletes the Keystore entry for [alias], if present.
     */
    fun deleteKey(alias: String) {
        synchronized(keyLock) {
            runCatching {
                if (keyStore.containsAlias(alias)) {
                    keyStore.deleteEntry(alias)
                }
            }
        }
    }

    /**
     * Computes a SHA-256 digest of [data].
     */
    fun sha256(data: ByteArray): ByteArray = MessageDigest.getInstance("SHA-256").digest(data)

    /**
     * Computes the lowercase hexadecimal SHA-256 digest of [text].
     */
    fun sha256Hex(text: String): String =
        sha256(text.toByteArray(Charsets.UTF_8)).joinToString("") { "%02x".format(it) }

    /**
     * Computes an HMAC-SHA256 authentication tag of [data] with [secretKey].
     */
    fun hmacSha256(secretKey: ByteArray, data: ByteArray): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secretKey, "HmacSHA256"))
        return mac.doFinal(data)
    }

    /**
     * Compares two byte arrays in constant time to resist timing side-channel attacks.
     */
    fun constantTimeEquals(a: ByteArray, b: ByteArray): Boolean = MessageDigest.isEqual(a, b)

    /**
     * Compares two strings in constant time over UTF-8 bytes.
     */
    fun constantTimeEquals(a: String, b: String): Boolean =
        constantTimeEquals(a.toByteArray(Charsets.UTF_8), b.toByteArray(Charsets.UTF_8))

    /**
     * Generates a cryptographically secure URL-safe random token backed by [SecureRandom].
     */
    fun generateSecureToken(byteLength: Int = 32): String {
        val bytes = ByteArray(byteLength.coerceAtLeast(1))
        secureRandom.nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }

    private fun getOrCreateKey(alias: String): SecretKey = synchronized(keyLock) {
        val entry = keyStore.getEntry(alias, null) as? KeyStore.SecretKeyEntry
        entry?.secretKey ?: generateKey(alias)
    }

    private fun generateKey(alias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )
        val spec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }
}
