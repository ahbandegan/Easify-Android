package ir.amirhesambandegan.easify_context

import android.content.Context
import android.util.TypedValue
import android.view.WindowManager

/**
 * Utility extensions for screen and dimension operations.
 */

/**
 * Gets the screen width in pixels.
 */
val Context.screenWidthPx: Int
    get() = resources.displayMetrics.widthPixels

/**
 * Gets the screen height in pixels.
 */
val Context.screenHeightPx: Int
    get() = resources.displayMetrics.heightPixels

/**
 * Converts DP (Density-independent Pixels) to exact Pixels.
 */
fun Context.dpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )
}

/**
 * Converts SP (Scale-independent Pixels) to exact Pixels.
 */
fun Context.spToPx(sp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        resources.displayMetrics
    )
}

/**
 * Turns the "Keep Screen On" flag on or off for the current activity.
 * Helpful for video players, barcode scanners, etc.
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
