package ir.amirhesambandegan.easify_haptic

import android.content.Context

/**
 * Tracks pull/drag gesture progress (`0.0f`..`1.0f`) and dispatches haptic ticks at 20% intervals.
 */
class PullHapticTracker(context: Context) {
    private val appContext = context.applicationContext
    private var lastTickThreshold = 0f

    fun onPullProgress(progress: Float) {
        val clamped = progress.coerceIn(0f, 1f)
        while (clamped >= lastTickThreshold + 0.2f && lastTickThreshold < 0.8f) {
            appContext.hapticTick()
            lastTickThreshold += 0.2f
        }
        while (clamped <= lastTickThreshold - 0.2f && lastTickThreshold > 0f) {
            lastTickThreshold = (lastTickThreshold - 0.2f).coerceAtLeast(0f)
        }

        if (clamped >= 1.0f && lastTickThreshold < 1.0f) {
            appContext.hapticImpactMedium()
            lastTickThreshold = 1.0f
        }
    }

    fun reset() {
        lastTickThreshold = 0f
    }
}
