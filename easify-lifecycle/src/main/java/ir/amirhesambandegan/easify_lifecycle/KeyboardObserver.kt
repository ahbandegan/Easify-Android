package ir.amirhesambandegan.easify_lifecycle

import android.app.Activity
import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

/**
 * A composable function that tracks the visibility and height of the soft keyboard.
 *
 * It uses an [android.view.ViewTreeObserver.OnGlobalLayoutListener] attached to the activity's
 * decor view to monitor layout changes and calculates the keyboard's height by comparing the
 * screen's height to the visible display frame.
 *
 * @return A [State] wrapping a [KeyboardInfo] object containing the current visibility status and height of the keyboard.
 */
@Composable
fun rememberKeyboardState(): State<KeyboardInfo> {
    val context = LocalContext.current
    val keyboardState = remember { mutableStateOf(KeyboardInfo(false, 0)) }
    
    DisposableEffect(context) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}
        val rootView = activity.window.decorView
        
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom
            
            val isVisible = keypadHeight > screenHeight * 0.15
            keyboardState.value = KeyboardInfo(isVisible, if (isVisible) keypadHeight else 0)
        }
        
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
        
        onDispose {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    
    return keyboardState
}

/**
 * Data class representing the current state of the soft keyboard.
 *
 * @property isVisible `true` if the keyboard is considered visible, `false` otherwise.
 * @property heightPx The height of the keyboard in pixels. It is `0` when the keyboard is not visible.
 */
data class KeyboardInfo(
    val isVisible: Boolean,
    val heightPx: Int
)
