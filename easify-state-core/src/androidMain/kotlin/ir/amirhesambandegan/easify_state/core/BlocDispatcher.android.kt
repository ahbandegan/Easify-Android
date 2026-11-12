package ir.amirhesambandegan.easify_state.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.EmptyCoroutineContext

internal actual fun createDefaultBlocDispatcher(): CoroutineDispatcher =
    try {
        Dispatchers.Main.immediate.also {
            it.isDispatchNeeded(EmptyCoroutineContext)
        }
    } catch (_: RuntimeException) {
        Dispatchers.Default
    }
