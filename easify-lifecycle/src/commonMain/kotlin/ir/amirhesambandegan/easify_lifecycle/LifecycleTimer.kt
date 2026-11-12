package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay

/**
 * Multiplatform composable timer that ticks every [intervalMillis] while lifecycle is in [activeState].
 */
@Composable
fun LifecycleTimer(
    intervalMillis: Long,
    activeState: Lifecycle.State = Lifecycle.State.RESUMED,
    onTick: () -> Unit
) {
    val safeInterval = intervalMillis.coerceAtLeast(1L)
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnTick = rememberUpdatedState(onTick)

    LaunchedEffect(lifecycleOwner, safeInterval, activeState) {
        lifecycleOwner.repeatOnLifecycle(activeState) {
            while (true) {
                delay(safeInterval)
                currentOnTick.value()
            }
        }
    }
}
