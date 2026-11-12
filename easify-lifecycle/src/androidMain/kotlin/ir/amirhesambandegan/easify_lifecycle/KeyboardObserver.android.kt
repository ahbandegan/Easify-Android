package ir.amirhesambandegan.easify_lifecycle

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Tracks soft keyboard visibility and height in pixels.
 */
@Composable
fun rememberKeyboardState(): State<KeyboardInfo> {
    val context = LocalContext.current
    val keyboardState = remember { mutableStateOf(KeyboardInfo(false, 0)) }

    DisposableEffect(context) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val rootView = activity.window.decorView

        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom
            val isVisible = screenHeight > 0 && keypadHeight > screenHeight * 0.15
            keyboardState.value = KeyboardInfo(isVisible, if (isVisible) keypadHeight else 0)
        }

        val observer = rootView.viewTreeObserver
        if (observer.isAlive) {
            observer.addOnGlobalLayoutListener(listener)
        }

        onDispose {
            val currentObserver = rootView.viewTreeObserver
            if (currentObserver.isAlive) {
                currentObserver.removeOnGlobalLayoutListener(listener)
            }
        }
    }

    return keyboardState
}

/**
 * Represents the current soft keyboard visibility and height in pixels.
 */
data class KeyboardInfo(
    val isVisible: Boolean,
    val heightPx: Int
)
