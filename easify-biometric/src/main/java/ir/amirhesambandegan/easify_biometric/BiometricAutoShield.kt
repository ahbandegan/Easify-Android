package ir.amirhesambandegan.easify_biometric

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Automatically shields the wrapped content and triggers [onUnlockRequest] when returning to
 * foreground (`ON_RESUME`) while locked, unless within [GracePeriodManager].
 * When locked, all touch/pointer input to underlying composables is strictly intercepted.
 */
@Composable
fun BiometricAutoShield(
    isUnlocked: Boolean,
    onUnlockRequest: () -> Unit,
    lockedOverlay: @Composable () -> Unit = {
        Text(text = "App Locked", color = Color.White)
    },
    content: @Composable () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val latestIsUnlocked by rememberUpdatedState(isUnlocked)
    val latestOnUnlockRequest by rememberUpdatedState(onUnlockRequest)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && !latestIsUnlocked) {
                if (!GracePeriodManager.isGracePeriodActive()) {
                    latestOnUnlockRequest()
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
                    .background(Color.Black.copy(alpha = 0.92f))
                    .semantics(mergeDescendants = true) { disabled() }
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                event.changes.forEach { it.consume() }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                lockedOverlay()
            }
        }
    }
}
