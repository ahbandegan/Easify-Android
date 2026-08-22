package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle

/**
 * A composable side-effect that executes a callback when the [Lifecycle.Event.ON_START] event occurs.
 *
 * Note: Internally, it is mapped to a lifecycle event, which triggers the provided callback.
 *
 * @param onStart The callback to execute when the lifecycle is started.
 */
@Composable
fun OnStartEffect(onStart: () -> Unit) = LifecycleEventEffect(Lifecycle.Event.ON_START, onStart)