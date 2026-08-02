package ir.amirhesambandegan.easify_biometric

import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import ir.amirhesambandegan.easify_context.findFragmentActivity

/**
 * A launcher for biometric authentication.
 */
class BiometricLauncher(
    private val activity: FragmentActivity,
    private val onResult: (BiometricResult) -> Unit
) {
    /**
     * Launches the biometric authentication prompt.
     *
     * @param title The title to display in the prompt.
     * @param subtitle The subtitle to display in the prompt.
     * @param description The description to display in the prompt.
     * @param negativeButtonText The text for the negative button (e.g., "Cancel").
     */
    fun launch(
        title: String,
        subtitle: String? = null,
        description: String? = null,
        negativeButtonText: String = "Cancel"
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onResult(BiometricResult.Error(errorCode, errString))
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onResult(BiometricResult.Success(result))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onResult(BiometricResult.Failed)
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setNegativeButtonText(negativeButtonText)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}

/**
 * Creates and remembers a [BiometricLauncher] to handle biometric authentication.
 *
 * @param onResult Callback invoked with the result of the authentication.
 * @return A [BiometricLauncher] instance.
 */
@Composable
fun rememberBiometricLauncher(
    onResult: (BiometricResult) -> Unit
): BiometricLauncher? {
    val context = LocalContext.current
    val activity = context.findFragmentActivity()

    return if (activity != null) {
        remember(activity) {
            BiometricLauncher(activity, onResult)
        }
    } else {
        null
    }
}
