package ir.amirhesambandegan.easify_biometric

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.biometric.BiometricManager

/**
 * Navigates safely to device biometric enrollment settings with automatic fallbacks for OEMs
 * that omit the dedicated biometric enrollment action.
 */
object EnrollmentNavigator {

    /**
     * Opens device biometric enrollment settings (Android 11+), falling back cleanly to security
     * or general system settings if an OEM omits the activity handler.
     */
    fun openBiometricEnrollmentSettings(context: Context) {
        val primaryIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
            }
        } else {
            Intent(Settings.ACTION_SECURITY_SETTINGS)
        }
        primaryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            context.startActivity(primaryIntent)
        } catch (_: ActivityNotFoundException) {
            val securityIntent = Intent(Settings.ACTION_SECURITY_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(securityIntent)
            } catch (_: ActivityNotFoundException) {
                runCatching {
                    context.startActivity(
                        Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            }
        }
    }
}
