package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * Returns a [State] indicating whether the Android application is currently in the foreground.
 */
@Composable
fun rememberAppForegroundState(): State<Boolean> {
    val processLifecycle = ProcessLifecycleOwner.get().lifecycle
    val isForeground = remember {
        mutableStateOf(processLifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
    }

    DisposableEffect(processLifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> isForeground.value = true
                Lifecycle.Event.ON_STOP -> isForeground.value = false
                else -> Unit
            }
        }
        isForeground.value = processLifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
        processLifecycle.addObserver(observer)

        onDispose {
            processLifecycle.removeObserver(observer)
        }
    }

    return isForeground
}
