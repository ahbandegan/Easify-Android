package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay

/**
 * A timer that ticks only when the lifecycle is in the given state (default RESUMED).
 * It automatically pauses when the user leaves the screen, and resumes when they return.
 */
@Composable
fun LifecycleTimer(
    intervalMillis: Long,
    activeState: Lifecycle.State = Lifecycle.State.RESUMED,
    onTick: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnTick = rememberUpdatedState(onTick)
    
    LaunchedEffect(lifecycleOwner, intervalMillis) {
        lifecycleOwner.repeatOnLifecycle(activeState) {
            while (true) {
                delay(intervalMillis)
                currentOnTick.value()
            }
        }
    }
}
