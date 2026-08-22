package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * Returns true if the entire application is in the foreground.
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
