package ir.amirhesambandegan.easify_biometric

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Utility object for providing haptic feedback during biometric authentication events.
 */
object BiometricHapticFeedback {

    /**
     * Plays a success haptic vibration to notify the user of successful authentication.
     * 
     * @param context The application or activity context used to access the vibrator service.
     */
    fun playSuccessVibration(context: Context) {
        val vibrator = getVibrator(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        } else {
            vibrator?.vibrate(50)
        }
    }

    /**
     * Plays an error haptic vibration to notify the user of failed authentication.
     * 
     * @param context The application or activity context used to access the vibrator service.
     */
    fun playErrorVibration(context: Context) {
        val vibrator = getVibrator(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))
        } else {
            val pattern = longArrayOf(0, 100, 50, 100)
            vibrator?.vibrate(pattern, -1)
        }
    }

    /**
     * Retrieves the appropriate [Vibrator] system service based on the current Android SDK version.
     * 
     * @param context The context used to fetch the service.
     * @return The [Vibrator] instance, or null if it cannot be retrieved.
     */
    private fun getVibrator(context: Context): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
}
