package ir.amirhesambandegan.easify_haptic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect

/**
 * Triggers a heartbeat-like haptic feedback pattern.
 * 
 * @receiver The context used to perform the haptic feedback.
 */
fun Context.hapticHeartbeat() {
    val pattern = longArrayOf(0, 40, 150, 40)
    val amplitudes = intArrayOf(0, 150, 0, 255)
    performPattern(pattern, amplitudes)
}

/**
 * Triggers a warning haptic feedback pattern.
 * 
 * @receiver The context used to perform the haptic feedback.
 */
fun Context.hapticWarning() {
    val pattern = longArrayOf(0, 50, 100, 50)
    val amplitudes = intArrayOf(0, 255, 0, 255)
    performPattern(pattern, amplitudes)
}

/**
 * Triggers an SOS haptic feedback pattern in Morse code.
 * 
 * @receiver The context used to perform the haptic feedback.
 */
fun Context.hapticSOS() {
    // S: 3 short, O: 3 long, S: 3 short
    val pattern = longArrayOf(
        0, 100, 100, 100, 100, 100, 
        300, 300, 100, 300, 100, 300, 
        300, 100, 100, 100, 100, 100
    )
    performPattern(pattern, null)
}

/**
 * Triggers a rapid, machine-gun style haptic feedback pattern.
 * 
 * @receiver The context used to perform the haptic feedback.
 */
fun Context.hapticMachineGun() {
    val pattern = longArrayOf(0, 30, 40, 30, 40, 30, 40, 30, 40, 30)
    val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255, 0, 255)
    performPattern(pattern, amplitudes)
}

/**
 * Internal method to perform a complex haptic waveform pattern.
 *
 * @receiver The context used to perform the haptic feedback.
 * @param pattern An array of timings (off/on sequences) in milliseconds.
 * @param amplitudes An array of amplitudes corresponding to the timings, or null to use default amplitude.
 */
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
