package ir.amirhesambandegan.easify_state.core

import kotlinx.coroutines.flow.StateFlow

/**
 * An entity that provides a stream of states via [state].
 * Modeled directly after Flutter's `StateStreamable`.
 */
interface StateStreamable<S : BlocState> {
    val state: StateFlow<S>
}
