package ir.amirhesambandegan.easify_biometric

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.content.ContextCompat

/**
 * Provides safe haptic feedback during biometric authentication events across Android API levels.
 */
object BiometricHapticFeedback {

    /**
     * Plays a crisp success haptic vibration if a vibrator hardware unit is available.
     */
    fun playSuccessVibration(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.VIBRATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                vibrator.vibrate(VibrationEffect.createOneShot(50L, VibrationEffect.DEFAULT_AMPLITUDE))
            }
            else -> {
                @Suppress("DEPRECATION")
                vibrator.vibrate(50L)
            }
        }
    }

    /**
     * Plays a distinct double-pulse error vibration when authentication fails.
     */
    fun playErrorVibration(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.VIBRATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                val pattern = longArrayOf(0L, 100L, 50L, 100L)
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
            }
            else -> {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0L, 100L, 50L, 100L), -1)
            }
        }
    }

    private fun getVibrator(context: Context): Vibrator? {
        val appContext = context.applicationContext
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }
}
