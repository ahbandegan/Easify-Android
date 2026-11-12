package ir.amirhesambandegan.easify_biometric

import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import ir.amirhesambandegan.easify_context.findFragmentActivity

/**
 * Launches standard system biometric authentication attached to a [FragmentActivity].
 */
class BiometricLauncher(
    private val activity: FragmentActivity,
    private val onResult: (BiometricResult) -> Unit
) {
    /**
     * Launches the biometric authentication dialog with the specified prompt texts.
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
 * Remembers a [BiometricLauncher] bound to the nearest [FragmentActivity], always invoking the
 * latest [onResult] lambda across recompositions.
 */
@Composable
fun rememberBiometricLauncher(
    onResult: (BiometricResult) -> Unit
): BiometricLauncher? {
    val context = LocalContext.current
    val activity = context.findFragmentActivity()
    val latestOnResult by rememberUpdatedState(onResult)

    return if (activity != null) {
        remember(activity) {
            BiometricLauncher(activity) { result ->
                latestOnResult(result)
            }
        }
    } else {
        null
    }
}
