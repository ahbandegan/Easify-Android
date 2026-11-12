package ir.amirhesambandegan.easify_haptic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.FloatRange

/**
 * Global configuration and hardware access manager for Easify haptic feedback.
 */
object EasifyHapticManager {
    /**
     * Global multiplier for haptic amplitude (`0.0f` muted .. `1.0f` full intensity).
     */
    @Volatile
    var globalIntensity: Float = 1.0f
        set(@FloatRange(from = 0.0, to = 1.0) value) {
            field = value.coerceIn(0f, 1f)
        }

    /**
     * Global kill switch for all Easify haptic feedback helpers.
     */
    @Volatile
    var isHapticEnabled: Boolean = true

    internal fun getVibrator(context: Context): Vibrator? {
        if (!isHapticEnabled || globalIntensity <= 0f) return null
        val appContext = context.applicationContext
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
        return vibrator?.takeIf { it.hasVibrator() }
    }

    /**
     * Scales [amplitude] by [globalIntensity] while preserving zero (`0`) silent slots in waveforms.
     */
    internal fun applyIntensity(amplitude: Int): Int {
        if (amplitude <= 0 || !isHapticEnabled || globalIntensity <= 0f) return 0
        if (amplitude == VibrationEffect.DEFAULT_AMPLITUDE) return amplitude
        return (amplitude * globalIntensity).toInt().coerceIn(1, 255)
    }
}
