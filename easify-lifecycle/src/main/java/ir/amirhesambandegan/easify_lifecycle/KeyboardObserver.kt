package ir.amirhesambandegan.easify_lifecycle

import android.app.Activity
import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

/**
 * Tracks the visibility and height of the soft keyboard.
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

data class KeyboardInfo(
    val isVisible: Boolean,
    val heightPx: Int
)
