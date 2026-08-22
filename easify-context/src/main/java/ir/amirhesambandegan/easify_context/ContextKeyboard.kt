package ir.amirhesambandegan.easify_context

import android.content.Context
import android.view.View

/**
 * Utility extensions for managing the software keyboard (Input Method).
 */

/**
 * Hides the software keyboard (Input Method) from the screen.
 * 
 * This extension function uses the [android.view.inputmethod.InputMethodManager] to request 
 * that the software keyboard be hidden, using the window token of the provided [view].
 * It is typically called when the user finishes typing or taps outside of an input field.
 *
 * @param view The [View] that is currently focused or in the same window, providing the 
 *             necessary `windowToken` to hide the keyboard. If this is null, the function 
 *             does nothing.
 */
fun Context.hideKeyboard(view: View?) {
    view?.let {
        inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

/**
 * Shows the software keyboard (Input Method) and forces focus on the given view.
 * 
 * This extension function first requests focus for the specified [view], ensuring it is ready 
 * to receive input. Then, it uses the [android.view.inputmethod.InputMethodManager] to display 
 * the software keyboard. This is often used when automatically focusing an `EditText` 
 * when a screen is opened.
 *
 * @param view The [View] (usually an `EditText`) that should receive focus and for which 
 *             the keyboard should be displayed. If this is null, the function does nothing.
 */
fun Context.showKeyboard(view: View?) {
    view?.let {
        it.requestFocus()
        inputMethodManager?.showSoftInput(it, 0)
    }
}
