package ir.amirhesambandegan.easify_haptic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect

fun Context.hapticHeartbeat() {
    val pattern = longArrayOf(0, 40, 150, 40)
    val amplitudes = intArrayOf(0, 150, 0, 255)
    performPattern(pattern, amplitudes)
}

fun Context.hapticWarning() {
    val pattern = longArrayOf(0, 50, 100, 50)
    val amplitudes = intArrayOf(0, 255, 0, 255)
    performPattern(pattern, amplitudes)
}

fun Context.hapticSOS() {
    // S: 3 short, O: 3 long, S: 3 short
    val pattern = longArrayOf(
        0, 100, 100, 100, 100, 100, 
        300, 300, 100, 300, 100, 300, 
        300, 100, 100, 100, 100, 100
    )
    performPattern(pattern, null)
}

fun Context.hapticMachineGun() {
    val pattern = longArrayOf(0, 30, 40, 30, 40, 30, 40, 30, 40, 30)
    val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255, 0, 255)
    performPattern(pattern, amplitudes)
}

private fun Context.performPattern(pattern: LongArray, amplitudes: IntArray?) {
    val vibrator = EasifyHapticManager.getVibrator(this) ?: return
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && amplitudes != null && vibrator.hasAmplitudeControl()) {
        val scaledAmps = amplitudes.map { EasifyHapticManager.applyIntensity(it) }.toIntArray()
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, scaledAmps, -1))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(pattern, -1)
    }
}
