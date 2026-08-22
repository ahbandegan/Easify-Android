package ir.amirhesambandegan.easify_biometric

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * A launcher that provides biometric authentication backed by a cryptographic key.
 * This ensures that the biometric authentication is strongly bound to a secure key in the Android Keystore.
 * 
 * @property activity The [FragmentActivity] used to host the biometric prompt.
 * @property onResult A callback invoked with the [BiometricResult] of the authentication attempt.
 */
class CryptoBiometricLauncher(
    private val activity: FragmentActivity,
    private val onResult: (BiometricResult) -> Unit
) {
    /** The alias used to store the secure key in the Keystore. */
    private val keyAlias = "EasifySecureKey"

    /**
     * Launches the biometric prompt with crypto-object binding.
     * 
     * @param title The title displayed on the prompt.
     * @param subtitle The optional subtitle displayed on the prompt.
     */
    fun launchCrypto(title: String, subtitle: String? = null) {
        try {
            val cipher = getInitializedCipher()
            val cryptoObject = BiometricPrompt.CryptoObject(cipher)

            val biometricPrompt = BiometricPrompt(
                activity,
                ContextCompat.getMainExecutor(activity),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        onResult(BiometricResult.Error(errorCode, errString))
                    }
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        onResult(BiometricResult.Success(result))
                    }
                    override fun onAuthenticationFailed() {
                        onResult(BiometricResult.Failed)
                    }
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setNegativeButtonText("Cancel")
                .setAllowedAuthenticators(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .build()

            biometricPrompt.authenticate(promptInfo, cryptoObject)
        } catch (e: Exception) {
            onResult(BiometricResult.Error(-1, e.message ?: "Crypto Error"))
        }
    }

    /**
     * Retrieves and initializes a [Cipher] using the secure key from the Keystore.
     * Generates a new key if one does not already exist.
     * 
     * @return An initialized [Cipher] for encryption.
     */
    private fun getInitializedCipher(): Cipher {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val key = keyStore.getKey(keyAlias, null) as? SecretKey ?: generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher
    }

    /**
     * Generates a new AES secret key in the Android Keystore that requires user authentication.
     * 
     * @return The newly generated [SecretKey].
     */
    private fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .build()
        )
        return keyGenerator.generateKey()
    }
}
