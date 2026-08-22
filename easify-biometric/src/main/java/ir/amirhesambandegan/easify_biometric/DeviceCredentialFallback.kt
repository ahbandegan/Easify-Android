package ir.amirhesambandegan.easify_biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object DeviceCredentialFallback {

    fun launchWithFallback(
        activity: FragmentActivity,
        title: String,
        subtitle: String? = null,
        onResult: (BiometricResult) -> Unit
    ) {
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

        // Note: When allowing DEVICE_CREDENTIAL, setNegativeButtonText MUST NOT be called.
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or 
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
