package ir.amirhesambandegan.easify_lifecycle

import android.app.Activity
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

/**
 * A composable side-effect that detects actual window focus changes.
 *
 * This is useful for detecting events like when the notification shade is pulled down,
 * a system dialog appears, or the user switches away from the app, which might not always
 * trigger a standard lifecycle pause event. It uses an [android.view.ViewTreeObserver.OnWindowFocusChangeListener].
 *
 * @param onFocusChanged The callback to execute when the window focus changes. Receives `true` if the window gains focus, `false` otherwise.
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
