package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle

@Composable
fun OnDestroyEffect(onDestroy: () -> Unit) = LifecycleEventEffect(Lifecycle.Event.ON_START, onDestroy)