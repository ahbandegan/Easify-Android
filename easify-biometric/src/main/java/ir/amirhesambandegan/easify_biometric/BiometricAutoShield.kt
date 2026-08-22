package ir.amirhesambandegan.easify_biometric

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

/**
 * A Composable that automatically shields the content and requests biometric authentication
 * when the app comes to the foreground (ON_RESUME), unless a grace period is active.
 *
 * @param isUnlocked Whether the content is currently unlocked.
 * @param onUnlockRequest Callback invoked to request an unlock (e.g., show biometric prompt).
 * @param content The actual content to display when unlocked or in the background.
 */
@Composable
fun BiometricAutoShield(
    isUnlocked: Boolean,
    onUnlockRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && !isUnlocked) {
                if (!GracePeriodManager.isGracePeriodActive()) {
                    onUnlockRequest()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        content()
        if (!isUnlocked) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "App Locked", color = Color.White)
            }
        }
    }
}
