package ir.amirhesambandegan.easify_context

import android.content.Context
import android.util.TypedValue
import android.view.WindowManager

/**
 * Utility extensions for screen dimensions, display metrics, and window management operations.
 */

/**
 * Retrieves the absolute width of the available display size in pixels.
 * 
 * This uses `resources.displayMetrics.widthPixels` to get the width of the screen.
 * Note that this might not include system decoration areas like the navigation bar depending 
 * on the exact Android version and window configuration.
 *
 * @return The screen width in raw pixels.
 */
val Context.screenWidthPx: Int
    get() = resources.displayMetrics.widthPixels

/**
 * Retrieves the absolute height of the available display size in pixels.
 * 
 * This uses `resources.displayMetrics.heightPixels` to get the height of the screen.
 * Note that this might not include system decoration areas like the status bar or navigation 
 * bar depending on the exact Android version and window configuration.
 *
 * @return The screen height in raw pixels.
 */
val Context.screenHeightPx: Int
    get() = resources.displayMetrics.heightPixels

/**
 * Converts a value from DP (Density-independent Pixels) to exact Pixels based on the device's display metrics.
 * 
 * This is crucial when working with APIs that require raw pixel values (like drawing on a Canvas 
 * or setting layout params programmatically) while maintaining a consistent physical size across 
 * different screen densities.
 *
 * @param dp The dimension value in DP to convert.
 * @return The dimension value converted to raw pixels as a [Float].
 */
fun Context.dpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )
}

/**
 * Converts a value from SP (Scale-independent Pixels) to exact Pixels based on the device's display metrics.
 * 
 * SP is similar to DP but is also scaled by the user's font size preference. This conversion is 
 * useful when setting text sizes programmatically in APIs that expect raw pixel values.
 *
 * @param sp The dimension value in SP to convert.
 * @return The dimension value converted to raw pixels as a [Float].
 */
fun Context.spToPx(sp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        resources.displayMetrics
    )
}

/**
 * Toggles the "Keep Screen On" flag for the window associated with the current activity.
 * 
 * This is particularly helpful for applications where you do not want the device to sleep or 
 * dim the screen while a process is running (e.g., video players, barcode scanners, reading modes).
 * 
 * It automatically attempts to unwrap the [Context] to find the underlying [android.app.Activity].
 * If no Activity can be found (e.g., if called from an Application context without a visible window),
 * this function does nothing.
 *
 * @param enable `true` to force the screen to stay on, `false` to clear the flag and allow 
 *               normal screen timeout behavior.
 */
fun Context.keepScreenOn(enable: Boolean) {
    val activity = findActivity()
    if (activity != null) {
        if (enable) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}
