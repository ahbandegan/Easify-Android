package ir.amirhesambandegan.easify_state.core

import kotlinx.coroutines.CoroutineDispatcher

internal expect fun createDefaultBlocDispatcher(): CoroutineDispatcher
