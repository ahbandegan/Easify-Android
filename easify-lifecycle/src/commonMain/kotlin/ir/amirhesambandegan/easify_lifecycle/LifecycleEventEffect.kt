package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Multiplatform composable side-effect that triggers [onEvent] when [event] occurs.
 */
@Composable
fun LifecycleEventEffect(
    event: Lifecycle.Event,
    onEvent: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnEvent by rememberUpdatedState(onEvent)

    DisposableEffect(lifecycleOwner, event) {
        val observer = LifecycleEventObserver { _, e ->
            if (e == event) {
                currentOnEvent()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

/**
 * Side-effect triggered on screen foreground (`ON_START`) and background (`ON_STOP`) transitions.
 */
@Composable
fun LifecycleTransitionEffect(
    onForeground: () -> Unit = {},
    onBackground: () -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentForeground by rememberUpdatedState(onForeground)
    val currentBackground by rememberUpdatedState(onBackground)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, e ->
            when (e) {
                Lifecycle.Event.ON_START -> currentForeground()
                Lifecycle.Event.ON_STOP -> currentBackground()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
