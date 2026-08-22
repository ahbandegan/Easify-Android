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

class CryptoBiometricLauncher(
    private val activity: FragmentActivity,
    private val onResult: (BiometricResult) -> Unit
) {
    private val keyAlias = "EasifySecureKey"

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

    private fun getInitializedCipher(): Cipher {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val key = keyStore.getKey(keyAlias, null) as? SecretKey ?: generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher
    }

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
