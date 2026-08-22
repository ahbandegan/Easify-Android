package ir.amirhesambandegan.easify_security

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import ir.amirhesambandegan.easify_context.findActivity

/**
 * Blurs or blanks out the app screen when sent to the recent apps list (multi-tasking view).
 * It achieves this by dynamically adding the [WindowManager.LayoutParams.FLAG_SECURE] flag 
 * to the window when the application is paused, and clearing it when resumed.
 */
@Composable
fun BackgroundPrivacyShield() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            val activity = context.findActivity() ?: return@LifecycleEventObserver
            val window = activity.window

            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
                Lifecycle.Event.ON_RESUME -> {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
