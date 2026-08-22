package ir.amirhesambandegan.easify_context

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Utility extensions for handling device vibrations.
 */

/**
 * Vibrates the device for a specified duration in milliseconds.
 * Requires <uses-permission android:name="android.permission.VIBRATE" />
 */
@SuppressLint("MissingPermission")
fun Context.vibrate(durationMillis: Long = 100) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    } ?: return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(durationMillis)
    }
}

/**
 * Vibrates the device using a specific pattern.
 * Pattern format: [delay, vibrate, sleep, vibrate, sleep...]
 */
@SuppressLint("MissingPermission")
fun Context.vibratePattern(pattern: LongArray, repeatIndex: Int = -1) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    } ?: return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeatIndex))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(pattern, repeatIndex)
    }
}
