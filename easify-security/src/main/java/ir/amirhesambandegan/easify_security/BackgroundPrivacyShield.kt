package ir.amirhesambandegan.easify_security

import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import ir.amirhesambandegan.easify_context.findActivity

/**
 * Hides sensitive app snapshots in the Android recent-apps switcher by acquiring [FLAG_SECURE]
 * when paused and releasing it when resumed, cooperating safely with [ScreenShield].
 */
@Composable
fun BackgroundPrivacyShield() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, context) {
        var window: Window? = context.findActivity()?.window
        var heldByBackgroundShield = false

        val observer = LifecycleEventObserver { _, event ->
            val currentWindow = context.findActivity()?.window ?: window
            window = currentWindow
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    if (!heldByBackgroundShield) {
                        WindowSecureFlagTracker.acquire(currentWindow)
                        heldByBackgroundShield = true
                    }
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (heldByBackgroundShield) {
                        WindowSecureFlagTracker.release(currentWindow)
                        heldByBackgroundShield = false
                    }
                }
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            if (heldByBackgroundShield) {
                WindowSecureFlagTracker.release(window)
                heldByBackgroundShield = false
            }
        }
    }
}
