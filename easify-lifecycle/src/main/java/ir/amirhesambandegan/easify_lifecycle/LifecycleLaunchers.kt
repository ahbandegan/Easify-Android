package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope

/**
 * Launches a coroutine that runs only when the lifecycle is at least in the [Lifecycle.State.RESUMED] state.
 *
 * The coroutine suspends its execution when the lifecycle state falls below [Lifecycle.State.RESUMED]
 * (e.g., when the lifecycle is paused) and resumes executing when it returns to [Lifecycle.State.RESUMED].
 *
 * @param key1 An optional key used to restart the effect when it changes.
 * @param block The suspending block to execute within the coroutine scope.
 */
@Composable
fun LaunchWhenResumed(
    key1: Any? = Unit,
    block: suspend CoroutineScope.() -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            block()
        }
    }
}

/**
 * Launches a coroutine that runs only when the lifecycle is at least in the [Lifecycle.State.STARTED] state.
 *
 * The coroutine suspends its execution when the lifecycle state falls below [Lifecycle.State.STARTED]
 * (e.g., when the lifecycle is stopped) and resumes executing when it returns to [Lifecycle.State.STARTED].
 *
 * @param key1 An optional key used to restart the effect when it changes.
 * @param block The suspending block to execute within the coroutine scope.
 */
@Composable
fun LaunchWhenStarted(
    key1: Any? = Unit,
    block: suspend CoroutineScope.() -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}
