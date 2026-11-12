package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope

/**
 * Launches a coroutine that runs only while the lifecycle is at least [Lifecycle.State.RESUMED].
 */
@Composable
fun LaunchWhenResumed(
    key1: Any? = Unit,
    block: suspend CoroutineScope.() -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentBlock by rememberUpdatedState(block)
    LaunchedEffect(key1, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            currentBlock()
        }
    }
}

/**
 * Launches a coroutine that runs only while the lifecycle is at least [Lifecycle.State.STARTED].
 */
@Composable
fun LaunchWhenStarted(
    key1: Any? = Unit,
    block: suspend CoroutineScope.() -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentBlock by rememberUpdatedState(block)
    LaunchedEffect(key1, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            currentBlock()
        }
    }
}
