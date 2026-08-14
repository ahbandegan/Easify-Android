package ir.amirhesambandegan.easify_security

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import ir.amirhesambandegan.easify_context.findActivity

/**
 * A Composable that prevents screenshots and screen recordings while it's in the composition.
 * It applies [WindowManager.LayoutParams.FLAG_SECURE] to the current window.
 */
@Composable
fun ScreenShield() {
    val context = LocalContext.current
    
    DisposableEffect(Unit) {
        val activity = context.findActivity()
        val window = activity?.window
        
        window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}

/**
 * A [Modifier] that prevents screenshots and screen recordings while the Composable it's applied to is active.
 * Note: This affects the entire window.
 */
fun Modifier.screenShield(): Modifier = composed {
    ScreenShield()
    this
}
