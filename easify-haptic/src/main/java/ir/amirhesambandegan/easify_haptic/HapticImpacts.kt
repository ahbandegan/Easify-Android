package ir.amirhesambandegan.easify_haptic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import androidx.core.content.ContextCompat

/**
 * Triggers a subtle light haptic impact.
 */
fun Context.hapticImpactLight() {
    performHaptic(timing = 15L, amplitude = 50, effectId = VibrationEffect.EFFECT_TICK)
}

/**
 * Triggers a standard medium haptic impact.
 */
fun Context.hapticImpactMedium() {
    performHaptic(timing = 25L, amplitude = 120, effectId = VibrationEffect.EFFECT_CLICK)
}

/**
 * Triggers a heavy haptic impact.
 */
fun Context.hapticImpactHeavy() {
    performHaptic(timing = 35L, amplitude = 255, effectId = VibrationEffect.EFFECT_HEAVY_CLICK)
}

/**
 * Triggers a crisp light tick haptic feedback.
 */
fun Context.hapticTick() {
    performHaptic(timing = 10L, amplitude = 30, effectId = VibrationEffect.EFFECT_TICK)
}

/**
 * Triggers a short key-press haptic pulse.
 */
fun Context.hapticKeypress() {
    performHaptic(timing = 12L, amplitude = 80, effectId = VibrationEffect.EFFECT_CLICK)
}

/**
 * Triggers an ascending double-tap confirmation haptic cue.
 */
fun Context.hapticConfirm() {
    if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.VIBRATE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    val vibrator = EasifyHapticManager.getVibrator(this) ?: return
    val pattern = longArrayOf(0L, 18L, 45L, 28L)
    val amplitudes = intArrayOf(0, 110, 0, 220)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator.hasAmplitudeControl()) {
        val scaled = amplitudes.map { EasifyHapticManager.applyIntensity(it) }.toIntArray()
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, scaled, -1))
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(pattern, -1)
    }
}

/**
 * Triggers a sharp rejection / validation-error haptic pulse sequence.
 */
fun Context.hapticReject() {
    if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.VIBRATE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    val vibrator = EasifyHapticManager.getVibrator(this) ?: return
    val pattern = longArrayOf(0L, 40L, 40L, 40L, 40L, 60L)
    val amplitudes = intArrayOf(0, 200, 0, 200, 0, 255)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator.hasAmplitudeControl()) {
        val scaled = amplitudes.map { EasifyHapticManager.applyIntensity(it) }.toIntArray()
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, scaled, -1))
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(pattern, -1)
    }
}

/**
 * Triggers distinct tactile feedback for switch/toggle state changes.
 */
fun Context.hapticToggle(isOn: Boolean) {
    if (isOn) hapticImpactMedium() else hapticImpactLight()
}

private fun Context.performHaptic(timing: Long, amplitude: Int, effectId: Int) {
    if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.VIBRATE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    val vibrator = EasifyHapticManager.getVibrator(this) ?: return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && EasifyHapticManager.globalIntensity >= 0.85f) {
        try {
            vibrator.vibrate(VibrationEffect.createPredefined(effectId))
            return
        } catch (_: Exception) {
            // Fall through to duration/amplitude based effect.
        }
    }

    val scaledAmplitude = EasifyHapticManager.applyIntensity(amplitude)
    if (scaledAmplitude <= 0) return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val amp = if (vibrator.hasAmplitudeControl()) scaledAmplitude else VibrationEffect.DEFAULT_AMPLITUDE
        vibrator.vibrate(VibrationEffect.createOneShot(timing, amp))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(timing)
    }
}
