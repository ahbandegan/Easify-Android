package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope

/**
 * Launches a coroutine that runs ONLY when the lifecycle is RESUMED.
 * It suspends when paused and resumes executing when resumed.
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
 * Launches a coroutine that runs ONLY when the lifecycle is STARTED.
 * It suspends when stopped and resumes executing when started.
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
