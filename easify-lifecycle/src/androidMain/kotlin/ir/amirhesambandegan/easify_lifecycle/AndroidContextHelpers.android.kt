package ir.amirhesambandegan.easify_lifecycle

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import java.util.Collections
import java.util.IdentityHashMap

internal fun Context.findActivity(): Activity? {
    val visited = Collections.newSetFromMap(IdentityHashMap<Context, Boolean>())
    var current: Context? = this
    while (current != null && visited.add(current)) {
        when (current) {
            is Activity -> return current
            is ContextWrapper -> current = current.baseContext
            else -> return null
        }
    }
    return null
}
