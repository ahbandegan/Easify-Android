package ir.amirhesambandegan.easify_state.core

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

/**
 * An [Emitter] is responsible for emitting new states from within an event handler.
 */
interface Emitter<S : BlocState> {
    /**
     * Emits the provided [state].
     */
    suspend fun emit(state: S)

    /**
     * Allows invoking the emitter directly as a function: `emit(nextState)`.
     * Mirroring Flutter's callable `emit` syntax.
     */
    suspend operator fun invoke(state: S) = emit(state)

    /**
     * Whether the event handler has completed or been cancelled.
     */
    val isDone: Boolean

    /**
     * Listens to a [flow] and emits states derived from each value or error.
     */
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