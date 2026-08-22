package ir.amirhesambandegan.easify_haptic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.FloatRange

/**
 * A singleton object that manages the configuration and state of the haptic feedback system.
 */
object EasifyHapticManager {
    /**
     * A global multiplier for the haptic intensity.
     * Ranges from 0.0 (no vibration) to 1.0 (full intensity).
     */
    var globalIntensity: Float = 1.0f
        set(@FloatRange(from = 0.0, to = 1.0) value) {
            field = value.coerceIn(0f, 1f)
        }
    
    /**
     * Indicates whether haptic feedback is globally enabled or disabled.
     */
    var isHapticEnabled: Boolean = true

    /**
     * Retrieves the system [Vibrator] service from the given [context].
     * Returns null if haptics are disabled globally.
     *
     * @param context The context used to access the vibrator service.
     * @return The [Vibrator] instance, or null if disabled.
     */
    internal fun getVibrator(context: Context): Vibrator? {
        if (!isHapticEnabled) return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    /**
     * Applies the [globalIntensity] multiplier to the given [amplitude].
     * Ensures the returned amplitude is within the valid range of 1 to 255.
     * 
     * @param amplitude The original amplitude value.
     * @return The scaled amplitude value based on the global intensity.
     */
    internal fun applyIntensity(amplitude: Int): Int {
        if (amplitude == VibrationEffect.DEFAULT_AMPLITUDE) return amplitude
        return (amplitude * globalIntensity).toInt().coerceIn(1, 255)
    }
}
