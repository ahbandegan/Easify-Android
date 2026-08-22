package ir.amirhesambandegan.easify_biometric

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.KeyPermanentlyInvalidatedException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Utility object for detecting if the user's biometric data has changed
 * since the last time this check was properly executed.
 */
object BiometricChangeDetector {

    /**
     * The alias used for storing the cryptographic key in the Android Keystore.
     */
    private const val KEY_ALIAS = "BiometricChangeCheckKey"

    /**
     * Checks if a new biometric (fingerprint/face) was added since the last check.
     * Returns true if biometrics were changed.
     * 
     * @return True if the biometric data has changed or if the key was invalidated, false otherwise.
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

    /**
     * Generates a new secret key for the Android Keystore that is tied to biometric enrollment.
     * If a new biometric is enrolled, this key becomes permanently invalidated.
     * 
     * @return A newly generated [SecretKey].
     */
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
