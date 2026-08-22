package ir.amirhesambandegan.easify_biometric

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

object EnrollmentNavigator {
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
