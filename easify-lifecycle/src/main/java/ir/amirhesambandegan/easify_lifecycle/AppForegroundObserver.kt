package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * Returns a [State] that indicates whether the entire application is currently in the foreground.
 *
 * This composable uses [ProcessLifecycleOwner] to track the application's lifecycle.
 * It sets the state to `true` when the application receives the [Lifecycle.Event.ON_START] event
 * and `false` when it receives the [Lifecycle.Event.ON_STOP] event.
 *
 * @return A [State] wrapping a [Boolean] value which is `true` if the app is in the foreground, `false` otherwise.
 */
@Composable
fun rememberAppForegroundState(): State<Boolean> {
    val isForeground = remember { mutableStateOf(false) }
    
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                isForeground.value = true
            } else if (event == Lifecycle.Event.ON_STOP) {
                isForeground.value = false
            }
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
        
        onDispose {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(observer)
        }
    }
    
    return isForeground
}
