package ir.amirhesambandegan.easify_storage

import android.content.Context
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.StreamingAead
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.streamingaead.StreamingAeadConfig
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * Provides authenticated streaming file encryption/decryption using **Google Tink `StreamingAead`**
 * (`AES256_GCM_HKDF_4KB`) with hardware-backed Android Keystore master key protection,
 * replacing the deprecated `androidx.security.crypto.EncryptedFile`.
 */
object SecureFileStorage {

    private const val MASTER_KEY_URI = "android-keystore://easify_tink_file_master_key"
    private const val KEYSET_NAME = "__easify_tink_streaming_file_keyset__"
    private const val PREF_FILE_NAME = "easify_tink_file_prefs"

    @Volatile
    private var initialized = false
    private val initLock = Any()

    @Volatile
    private var cachedStreamingAead: StreamingAead? = null

    /**
     * Returns the singleton Tink [StreamingAead] primitive bound to an Android Keystore master key.
     */
    fun getStreamingAead(context: Context): StreamingAead {
        cachedStreamingAead?.let { return it }
        synchronized(initLock) {
            cachedStreamingAead?.let { return it }
            if (!initialized) {
                StreamingAeadConfig.register()
                initialized = true
            }
            val appContext = context.applicationContext
            val keysetHandle = AndroidKeysetManager.Builder()
                .withSharedPref(appContext, KEYSET_NAME, PREF_FILE_NAME)
                .withKeyTemplate(KeyTemplates.get("AES256_GCM_HKDF_4KB"))
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()
                .keysetHandle
            return keysetHandle.getPrimitive(
                RegistryConfiguration.get(),
                StreamingAead::class.java
            ).also { cachedStreamingAead = it }
        }
    }

    /**
     * Opens an authenticated encrypting [OutputStream] writing to [file], binding [associatedData]
     * (by default `file.name`) as AAD.
     */
    fun openEncryptedOutputStream(
        context: Context,
        file: File,
        associatedData: ByteArray = file.name.toByteArray(Charsets.UTF_8)
    ): OutputStream {
        file.parentFile?.mkdirs()
        return getStreamingAead(context).newEncryptingStream(file.outputStream(), associatedData)
    }

    /**
     * Opens an authenticated decrypting [InputStream] reading from [file] with matching [associatedData].
     */
    fun openEncryptedInputStream(
        context: Context,
        file: File,
        associatedData: ByteArray = file.name.toByteArray(Charsets.UTF_8)
    ): InputStream {
        return getStreamingAead(context).newDecryptingStream(file.inputStream(), associatedData)
    }

    /**
     * Encrypts and writes raw [data] into [file] using Tink [StreamingAead].
     */
    fun writeSecureBytes(
        context: Context,
        file: File,
        data: ByteArray,
        associatedData: ByteArray = file.name.toByteArray(Charsets.UTF_8)
    ) {
        openEncryptedOutputStream(context, file, associatedData).use { output ->
            output.write(data)
        }
    }

    /**
     * Reads and decrypts raw bytes from [file] using Tink [StreamingAead].
     */
    fun readSecureBytes(
        context: Context,
        file: File,
        associatedData: ByteArray = file.name.toByteArray(Charsets.UTF_8)
    ): ByteArray {
        return openEncryptedInputStream(context, file, associatedData).use { input ->
            input.readBytes()
        }
    }

    /**
     * Encrypts and writes UTF-8 [content] securely into [file].
     */
    fun writeSecureString(context: Context, file: File, content: String) {
        writeSecureBytes(context, file, content.toByteArray(Charsets.UTF_8))
    }

    /**
     * Reads and decrypts UTF-8 text from [file].
     */
    fun readSecureString(context: Context, file: File): String {
        return String(readSecureBytes(context, file), Charsets.UTF_8)
    }
}
