package ir.amirhesambandegan.easify_haptic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.FloatRange

object EasifyHapticManager {
    var globalIntensity: Float = 1.0f
        set(@FloatRange(from = 0.0, to = 1.0) value) {
            field = value.coerceIn(0f, 1f)
        }
    
    var isHapticEnabled: Boolean = true

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

    internal fun applyIntensity(amplitude: Int): Int {
        if (amplitude == VibrationEffect.DEFAULT_AMPLITUDE) return amplitude
        return (amplitude * globalIntensity).toInt().coerceIn(1, 255)
    }
}
