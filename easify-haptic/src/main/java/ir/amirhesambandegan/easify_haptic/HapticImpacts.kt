package ir.amirhesambandegan.easify_haptic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect

fun Context.hapticImpactLight() {
    performHaptic(timing = 15, amplitude = 50, effectId = 2) // EFFECT_TICK or similar light fallback
}

fun Context.hapticImpactMedium() {
    performHaptic(timing = 25, amplitude = 120, effectId = 0) // EFFECT_CLICK
}

fun Context.hapticImpactHeavy() {
    performHaptic(timing = 35, amplitude = 255, effectId = 5) // EFFECT_HEAVY_CLICK
}

fun Context.hapticTick() {
    performHaptic(timing = 10, amplitude = 30, effectId = 2) // EFFECT_TICK
}

fun Context.hapticKeypress() {
    performHaptic(timing = 12, amplitude = 80, effectId = 0) // EFFECT_CLICK
}

private fun Context.performHaptic(timing: Long, amplitude: Int, effectId: Int) {
    val vibrator = EasifyHapticManager.getVibrator(this) ?: return
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        try {
            vibrator.vibrate(VibrationEffect.createPredefined(effectId))
            return
        } catch (e: Exception) {
            // Fallback
        }
    }
    
    val scaledAmplitude = EasifyHapticManager.applyIntensity(amplitude)
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (vibrator.hasAmplitudeControl()) {
            vibrator.vibrate(VibrationEffect.createOneShot(timing, scaledAmplitude))
        } else {
            vibrator.vibrate(VibrationEffect.createOneShot(timing, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(timing)
    }
}
