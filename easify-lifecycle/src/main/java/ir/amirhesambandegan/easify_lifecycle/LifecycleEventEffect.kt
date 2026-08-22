package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * A composable side-effect that triggers a callback when a specific [Lifecycle.Event] occurs.
 *
 * It attaches a [LifecycleEventObserver] to the [LocalLifecycleOwner] and executes the provided
 * [onEvent] callback whenever the observed lifecycle event matches the given [event].
 *
 * @param event The [Lifecycle.Event] to listen for (e.g., [Lifecycle.Event.ON_CREATE], [Lifecycle.Event.ON_RESUME]).
 * @param onEvent The callback to be invoked when the specified lifecycle event occurs.
 */
@Composable
fun LifecycleEventEffect(
    event: Lifecycle.Event,
    onEvent: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnEvent by rememberUpdatedState(onEvent)

    DisposableEffect(lifecycleOwner) {
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