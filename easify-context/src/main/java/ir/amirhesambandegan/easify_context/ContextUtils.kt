package ir.amirhesambandegan.easify_context

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity

/**
 * General utility extensions for [Context] manipulation and unwrapping.
 */

/**
 * Attempts to recursively unwrap a [ContextWrapper] to find the underlying [Activity].
 * 
 * In Android, a `Context` can often be wrapped multiple times (e.g., `TintContextWrapper`, 
 * `ContextThemeWrapper`). This function drills down through the `.baseContext` chain 
 * until it either finds an instance of [Activity] or runs out of wrappers.
 * 
 * This is particularly useful in Views or Adapters where you are handed a `Context` 
 * and need to perform Activity-specific operations (like showing dialogs, manipulating 
 * the window, or requesting permissions) without resorting to risky forced casts.
 *
 * @return The underlying [Activity] if found in the wrapper chain, or `null` if the context 
 *         is not tied to an Activity (e.g., an Application or Service context).
 */
fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) return currentContext
        currentContext = currentContext.baseContext
    }
    return null
}

/**
 * Attempts to recursively unwrap a [ContextWrapper] to find the underlying [FragmentActivity].
 * 
 * Similar to [findActivity], but specifically targets [FragmentActivity]. This is required 
 * for operations that depend on the AndroidX fragment system, such as using `SupportFragmentManager`,
 * `ViewModelProviders`, or displaying AndroidX `BiometricPrompt` dialogs.
 *
 * @return The underlying [FragmentActivity] if found, or `null` if the context is not 
 *         tied to a FragmentActivity.
 */
fun Context.findFragmentActivity(): FragmentActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is FragmentActivity) return currentContext
        currentContext = currentContext.baseContext
    }
    return null
}
