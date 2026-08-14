package ir.amirhesambandegan.easify_security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * A utility class to handle encryption and decryption using the Android Keystore system.
 * Uses AES-GCM encryption for high security and data integrity.
 */
object EncryptionManager {

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val IV_SIZE = 12 // 12 bytes for GCM IV

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        load(null)
    }

    /**
     * Encrypts the provided [text] using a key associated with the given [alias].
     *
     * @param alias The unique identifier for the key in the Keystore.
     * @param text The plain text to encrypt.
     * @return A Base64 encoded string containing the IV and the encrypted data, or an empty string on error.
     */
    fun encrypt(alias: String, text: String): String {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey(alias))
            
            val iv = cipher.iv
            val encryptedBytes = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
            
            // Prepend IV to the encrypted data for use in decryption
            val combined = ByteArray(iv.size + encryptedBytes.size)
            System.arraycopy(iv, 0, combined, 0, iv.size)
            System.arraycopy(encryptedBytes, 0, combined, iv.size, encryptedBytes.size)
            
            Base64.encodeToString(combined, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Decrypts the provided [encryptedText] using the key associated with the given [alias].
     *
     * @param alias The unique identifier for the key in the Keystore.
     * @param encryptedText The Base64 encoded string containing the IV and the encrypted data.
     * @return The original plain text, or null if decryption fails.
     */
    fun decrypt(alias: String, encryptedText: String): String? {
        return try {
            val combined = Base64.decode(encryptedText, Base64.DEFAULT)
            if (combined.size < IV_SIZE) return null
            
            val iv = combined.sliceArray(0 until IV_SIZE)
            val encryptedBytes = combined.sliceArray(IV_SIZE until combined.size)
            
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(alias), spec)
            
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Retrieves an existing [SecretKey] from the Keystore or generates a new one.
     */
    private fun getOrCreateKey(alias: String): SecretKey {
        val entry = keyStore.getEntry(alias, null) as? KeyStore.SecretKeyEntry
        return entry?.secretKey ?: generateKey(alias)
    }

    /**
     * Generates a new AES [SecretKey] in the Android Keystore.
     */
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
