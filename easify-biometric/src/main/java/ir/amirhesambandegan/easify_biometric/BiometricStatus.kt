package ir.amirhesambandegan.easify_biometric

import androidx.biometric.BiometricPrompt

/**
 * Sealed class representing the result of a biometric authentication attempt.
 */
sealed class BiometricResult {
    /**
     * Authentication was successful.
     * @property result The details of the successful authentication.
     */
    data class Success(val result: BiometricPrompt.AuthenticationResult) : BiometricResult()

    /**
     * An error occurred during authentication (e.g., no biometrics enrolled, hardware not available).
     * @property errorCode The error code returned by the system.
     * @property errString A human-readable error message.
     */
    data class Error(val errorCode: Int, val errString: CharSequence) : BiometricResult()

    /**
     * The biometric was recognized but not accepted, or the user canceled the prompt.
     */
    data object Failed : BiometricResult()
}
