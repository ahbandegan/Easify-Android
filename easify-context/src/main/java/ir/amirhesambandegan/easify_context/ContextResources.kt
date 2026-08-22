package ir.amirhesambandegan.easify_context

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * Utility extensions for easy resource access without ContextCompat boilerplate.
 */

/**
 * Gets a resolved color integer from a color resource ID.
 */
fun Context.color(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

/**
 * Gets a Drawable from a drawable resource ID.
 */
fun Context.drawable(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

/**
 * Gets an exact pixel size from a dimension resource ID.
 */
fun Context.dimenPx(@DimenRes id: Int): Int {
    return resources.getDimensionPixelSize(id)
}

/**
 * Gets a float dimension from a dimension resource ID.
 */
fun Context.dimen(@DimenRes id: Int): Float {
    return resources.getDimension(id)
}
