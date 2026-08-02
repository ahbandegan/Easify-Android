package ir.amirhesambandegan.easify_biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL

/**
 * Extension properties and functions for Biometric utilities.
 */
object BiometricUtils {

    /**
     * Checks if the device is capable of biometric authentication.
     *
     * @param context The application context.
     * @param authenticators The types of authenticators to check for. Defaults to Strong Biometric or Device Credential.
     * @return True if the device can authenticate, false otherwise.
     */
    fun canAuthenticate(
        context: Context,
        authenticators: Int = BIOMETRIC_STRONG or DEVICE_CREDENTIAL
    ): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS
    }
}
