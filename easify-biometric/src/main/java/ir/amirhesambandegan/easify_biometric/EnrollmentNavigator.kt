package ir.amirhesambandegan.easify_biometric

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

/**
 * Utility object for navigating the user to the device's biometric enrollment settings.
 */
object EnrollmentNavigator {
    
    /**
     * Opens the device settings screen where the user can enroll biometric data.
     * For devices running Android 11 (API 30) or higher, it opens the specific biometric enroll intent.
     * For older devices, it opens the general security settings.
     * 
     * @param context The context used to start the settings activity.
     */
    fun openBiometricEnrollmentSettings(context: Context) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, 
                    androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or 
                    androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            }
        } else {
            Intent(Settings.ACTION_SECURITY_SETTINGS)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
