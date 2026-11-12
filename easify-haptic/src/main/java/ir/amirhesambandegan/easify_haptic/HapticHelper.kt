package ir.amirhesambandegan.easify_haptic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import androidx.core.content.ContextCompat

/**
 * Utility functions for common multi-pulse tactile haptic patterns.
 */
object HapticHelper {

    fun Context.hapticClick() {
        vibrate(longArrayOf(0L, 10L))
    }

    fun Context.hapticSuccess() {
        vibrate(longArrayOf(0L, 20L, 100L, 20L))
    }

    fun Context.hapticError() {
        vibrate(longArrayOf(0L, 50L, 100L, 50L, 100L, 50L))
    }

    private fun Context.vibrate(pattern: LongArray) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.VIBRATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val vibrator = EasifyHapticManager.getVibrator(this) ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, -1)
        }
    }
}
