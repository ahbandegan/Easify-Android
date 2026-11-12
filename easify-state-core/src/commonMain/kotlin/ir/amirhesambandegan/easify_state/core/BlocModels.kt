package ir.amirhesambandegan.easify_state.core

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch

interface BlocEvent

interface BlocState

interface BlocEffect

/**
 * Responsible for emitting new states from within a [Bloc] event handler.
 */
interface Emitter<S : BlocState> {
    suspend fun emit(state: S)

    /** Callable shorthand allowing `emit(nextState)` inside handlers. */
    suspend operator fun invoke(state: S) = emit(state)

    /** Whether the handler has completed or the [Bloc] has been closed/cancelled. */
    val isDone: Boolean

    /** Collects [flow] and emits states produced by [onData] (and optional [onError]). */
    suspend fun <T> onEach(
        flow: Flow<T>,
        onData: suspend (T) -> S,
        onError: (suspend (Throwable) -> S)? = null
    ) {
        flow
            .catch { error ->
                if (error is CancellationException) throw error
                if (onError != null) {
                    emit(onError(error))
                } else {
                    throw error
                }
            }
            .collect { value ->
                emit(onData(value))
            }
    }
}

data class Change<S : BlocState>(
    val currentState: S,
    val nextState: S
)

data class Transition<E : BlocEvent, S : BlocState>(
    val currentState: S,
    val event: E,
    val nextState: S
) {
    fun toChange(): Change<S> = Change(currentState, nextState)
}

interface StateStreamable<S : BlocState> {
    val state: StateFlow<S>
}
