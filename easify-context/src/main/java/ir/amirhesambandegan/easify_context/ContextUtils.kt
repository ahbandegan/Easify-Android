package ir.amirhesambandegan.easify_context

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity
import java.util.Collections
import java.util.IdentityHashMap

/**
 * Recursively unwraps a [ContextWrapper] chain to locate the underlying [Activity] with cycle protection.
 */
fun Context.findActivity(): Activity? {
    var currentContext: Context? = this
    val visited = Collections.newSetFromMap(IdentityHashMap<Context, Boolean>())
    while (currentContext is ContextWrapper && visited.add(currentContext)) {
        if (currentContext is Activity) return currentContext
        currentContext = currentContext.baseContext
    }
    return null
}

/**
 * Recursively unwraps a [ContextWrapper] chain to locate the underlying [FragmentActivity] with cycle protection.
 */
fun Context.findFragmentActivity(): FragmentActivity? {
    var currentContext: Context? = this
    val visited = Collections.newSetFromMap(IdentityHashMap<Context, Boolean>())
    while (currentContext is ContextWrapper && visited.add(currentContext)) {
        if (currentContext is FragmentActivity) return currentContext
        currentContext = currentContext.baseContext
    }
    return null
}
