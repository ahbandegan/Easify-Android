package ir.amirhesambandegan.easify_lifecycle

import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

/**
 * Observes actual window focus changes (e.g. notification shade, system dialogs).
 */
@Composable
fun OnWindowFocusChangedEffect(onFocusChanged: (hasFocus: Boolean) -> Unit) {
    val context = LocalContext.current
    val currentOnFocusChanged = rememberUpdatedState(onFocusChanged)

    DisposableEffect(context) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val view = activity.window.decorView

        val listener = ViewTreeObserver.OnWindowFocusChangeListener { hasFocus ->
            currentOnFocusChanged.value(hasFocus)
        }

        val observer = view.viewTreeObserver
        if (observer.isAlive) {
            observer.addOnWindowFocusChangeListener(listener)
        }

        onDispose {
            val currentObserver = view.viewTreeObserver
            if (currentObserver.isAlive) {
                currentObserver.removeOnWindowFocusChangeListener(listener)
            }
        }
    }
}
