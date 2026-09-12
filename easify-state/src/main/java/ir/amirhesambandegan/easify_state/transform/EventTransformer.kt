package ir.amirhesambandegan.easify_state.transform

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import java.util.concurrent.atomic.AtomicBoolean

/**
 * An [EventTransformer] transforms the incoming flow of events into an outgoing flow.
 */
typealias EventTransformer<E> = (events: Flow<E>, mapper: (E) -> Flow<Unit>) -> Flow<Unit>

/**
 * Common event transformers mirroring `bloc_concurrency`.
 */
object Transformers {
    /**
     * Processes events concurrently (in parallel).
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun <E> concurrent(): EventTransformer<E> = { events, mapper ->
        events.flatMapMerge { mapper(it) }
    }

    /**
     * Processes events sequentially (one by one, FIFO queue).
     * This is the standard default behavior in flutter_bloc.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun <E> sequential(): EventTransformer<E> = { events, mapper ->
        events.flatMapConcat { mapper(it) }
    }

    /**
     * Ignores/drops incoming events while the current event is still being processed.
     * Useful for actions like login/submit buttons to prevent duplicate triggers.
     */
    fun <E> droppable(): EventTransformer<E> = { events, mapper ->
        flow {
            val busy = AtomicBoolean(false)
            events.collect { event ->
                if (busy.compareAndSet(false, true)) {
                    try {
                        mapper(event).collect { emit(Unit) }
                    } finally {
                        busy.set(false)
                    }
                }
            }
        }
    }

    /**
     * Cancels the current running event task whenever a new event arrives.
     * Useful for search inputs, type-ahead filters, and debounce.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun <E> restartable(): EventTransformer<E> = { events, mapper ->
        events.flatMapLatest { mapper(it) }
    }
}