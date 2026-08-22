package ir.amirhesambandegan.easify_context

import android.content.Context
import android.widget.Toast

/**
 * Utility extensions for displaying Toasts quickly.
 */

/**
 * Shows a short duration Toast.
 */
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Shows a long duration Toast.
 */
fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
