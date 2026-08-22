package ir.amirhesambandegan.easify_context

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * Utility extensions for easy and safe access to application resources.
 * 
 * These extensions wrap common [ContextCompat] or `Resources` calls to reduce boilerplate
 * when fetching colors, drawables, and dimensions.
 */

/**
 * Gets a resolved color integer from a color resource ID.
 * 
 * Uses [ContextCompat.getColor] to safely retrieve the color value, ensuring compatibility
 * across different Android API levels.
 *
 * @param id The resource ID of the color to retrieve (e.g., `R.color.primary`).
 * @return A single color value in the form `0xAARRGGBB`.
 */
fun Context.color(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

/**
 * Gets a Drawable from a drawable resource ID.
 * 
 * Uses [ContextCompat.getDrawable] to safely retrieve the drawable, ensuring compatibility
 * across different Android API levels (like handling vector drawables correctly on older APIs).
 *
 * @param id The resource ID of the drawable to retrieve (e.g., `R.drawable.ic_logo`).
 * @return The [Drawable] object associated with the resource, or `null` if it cannot be resolved.
 */
fun Context.drawable(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

/**
 * Gets an exact pixel size from a dimension resource ID.
 * 
 * This method retrieves the dimension and truncates it to an integer pixel size.
 * It is commonly used when a size must be an exact number of pixels (like width or height).
 *
 * @param id The resource ID of the dimension (e.g., `R.dimen.padding`).
 * @return The dimension converted to raw pixels as an [Int].
 */
fun Context.dimenPx(@DimenRes id: Int): Int {
    return resources.getDimensionPixelSize(id)
}

/**
 * Gets a float dimension from a dimension resource ID.
 * 
 * This method retrieves the dimension exactly as a float, without rounding or truncating.
 * It is commonly used for values like text size or fractional layout coordinates.
 *
 * @param id The resource ID of the dimension (e.g., `R.dimen.text_size`).
 * @return The dimension converted to raw pixels as a [Float].
 */
fun Context.dimen(@DimenRes id: Int): Float {
    return resources.getDimension(id)
}
