package ir.amirhesambandegan.easify_lifecycle

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle

@Composable
fun OnCreateEffect(onCreate: () -> Unit) = LifecycleEventEffect(Lifecycle.Event.ON_START, onCreate)