package ir.amirhesambandegan.easify_context

import android.content.Context
import android.view.View

/**
 * Utility extensions for managing the software keyboard.
 */

/**
 * Hides the software keyboard.
 * @param view The view that is currently focused.
 */
fun Context.hideKeyboard(view: View?) {
    view?.let {
        inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

/**
 * Shows the software keyboard and forces focus on the given view.
 * @param view The view that should receive focus.
 */
fun Context.showKeyboard(view: View?) {
    view?.let {
        it.requestFocus()
        inputMethodManager?.showSoftInput(it, 0)
    }
}
