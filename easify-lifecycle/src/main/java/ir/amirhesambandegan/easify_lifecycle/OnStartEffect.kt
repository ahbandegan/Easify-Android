package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle

@Composable
fun OnStartEffect(onStart: () -> Unit) = LifecycleEventEffect(Lifecycle.Event.ON_START, onStart)