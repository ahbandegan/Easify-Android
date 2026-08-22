package ir.amirhesambandegan.easify_haptic

import android.content.Context

/**
 * Tracks the progress of a pull/drag gesture (e.g., Pull to Refresh)
 * and fires haptic feedback at specific thresholds.
 */
class PullHapticTracker(private val context: Context) {
    private var lastTickThreshold = 0f

    /**
     * Call this inside a gesture listener. 
     * @param progress 0.0 to 1.0
     */
    fun onPullProgress(progress: Float) {
        if (progress > lastTickThreshold + 0.2f) {
            context.hapticTick()
            lastTickThreshold += 0.2f
        } else if (progress < lastTickThreshold - 0.2f) {
            lastTickThreshold -= 0.2f
        }
        
        if (progress >= 1.0f && lastTickThreshold < 1.0f) {
            context.hapticImpactMedium() // Snap threshold reached
            lastTickThreshold = 1.0f
        }
    }
    
    fun reset() {
        lastTickThreshold = 0f
    }
}
