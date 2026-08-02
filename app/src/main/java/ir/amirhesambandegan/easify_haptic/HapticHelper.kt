package ir.amirhesambandegan.easify_haptic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

/**
 * Utility functions for providing tactile haptic feedback.
 */
object HapticHelper {

    /**
     * Performs a standard click haptic feedback.
     */
    fun Context.hapticClick() {
        vibrate(longArrayOf(0, 10))
    }

    /**
     * Performs a "success" haptic feedback (soft double pulse).
     */
    fun Context.hapticSuccess() {
        vibrate(longArrayOf(0, 20, 100, 20))
    }

    /**
     * Performs an "error" haptic feedback (strong triple pulse).
     */
    fun Context.hapticError() {
        vibrate(longArrayOf(0, 50, 100, 50, 100, 50))
    }

    private fun Context.vibrate(pattern: LongArray) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, -1)
        }
    }
}
