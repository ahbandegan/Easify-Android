package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay

/**
 * A composable timer that ticks at a specified interval, but only when the lifecycle is in the specified active state.
 *
 * It automatically suspends execution when the user leaves the screen (or the lifecycle state falls below the
 * specified [activeState]) and resumes when the screen becomes active again.
 *
 * @param intervalMillis The time interval in milliseconds between consecutive ticks.
 * @param activeState The minimum [Lifecycle.State] required for the timer to tick. Defaults to [Lifecycle.State.RESUMED].
 * @param onTick The callback to execute on every tick of the timer.
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
