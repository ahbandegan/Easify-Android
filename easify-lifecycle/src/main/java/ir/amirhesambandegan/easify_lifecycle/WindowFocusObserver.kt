package ir.amirhesambandegan.easify_lifecycle

import android.app.Activity
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

/**
 * Detects actual window focus changes, e.g., when the notification shade is pulled down
 * or a system dialog appears, which does not always trigger onPause.
 */
@Composable
fun OnWindowFocusChangedEffect(onFocusChanged: (hasFocus: Boolean) -> Unit) {
    val context = LocalContext.current
    val currentOnFocusChanged = rememberUpdatedState(onFocusChanged)

    DisposableEffect(context) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}
        val view = activity.window.decorView
        
        val listener = ViewTreeObserver.OnWindowFocusChangeListener { hasFocus ->
            currentOnFocusChanged.value(hasFocus)
        }
        
        view.viewTreeObserver.addOnWindowFocusChangeListener(listener)
        
        onDispose {
            view.viewTreeObserver.removeOnWindowFocusChangeListener(listener)
        }
    }
}
