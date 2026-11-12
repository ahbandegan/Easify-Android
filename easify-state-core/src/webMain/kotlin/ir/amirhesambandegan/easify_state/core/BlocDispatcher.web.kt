package ir.amirhesambandegan.easify_state.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual fun createDefaultBlocDispatcher(): CoroutineDispatcher = Dispatchers.Default
