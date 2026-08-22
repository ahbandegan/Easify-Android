package ir.amirhesambandegan.easify_biometric

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.KeyPermanentlyInvalidatedException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object BiometricChangeDetector {

    private const val KEY_ALIAS = "BiometricChangeCheckKey"

    /**
     * Checks if a new biometric (fingerprint/face) was added since the last check.
     * Returns true if biometrics were changed.
     */
    fun hasBiometricChanged(): Boolean {
        try {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            var key = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
            if (key == null) {
                key = generateKey()
            }
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return false // Key is valid, no change
        } catch (e: KeyPermanentlyInvalidatedException) {
            return true // Key invalidated! New biometric was added!
        } catch (e: Exception) {
            return false
        }
    }

    private fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true) // Crucial for detection
            .build()
        )
        return keyGenerator.generateKey()
    }
}
