package ir.amirhesambandegan.easify_context

import android.content.Context
import android.widget.Toast

/**
 * Utility extensions for displaying brief on-screen messages (Toasts) quickly and concisely.
 */

/**
 * Displays a standard, short-duration Toast message.
 * 
 * This uses [Toast.LENGTH_SHORT], which displays the message for a brief moment
 * (typically around 2 seconds) before fading out automatically.
 *
 * @param message The text string to be displayed inside the Toast.
 */
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Displays a longer-duration Toast message.
 * 
 * This uses [Toast.LENGTH_LONG], which displays the message for a longer duration
 * (typically around 3.5 seconds) before fading out automatically.
 *
 * @param message The text string to be displayed inside the Toast.
 */
fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
