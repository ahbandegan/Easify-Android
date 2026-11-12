package ir.amirhesambandegan.easify_state.transform

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex

/**
 * An [EventTransformer] transforms the incoming flow of events into an outgoing flow.
 */
typealias EventTransformer<E> = (events: Flow<E>, mapper: (E) -> Flow<Unit>) -> Flow<Unit>

/**
 * Common multiplatform event transformers mirroring `bloc_concurrency`.
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
     * Default behavior in Flutter `bloc`.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun <E> sequential(): EventTransformer<E> = { events, mapper ->
        events.flatMapConcat { mapper(it) }
    }

    /**
     * Ignores incoming events while the current event is still being processed.
     */
    fun <E> droppable(): EventTransformer<E> = { events, mapper ->
        flow {
            val mutex = Mutex()
            events.collect { event ->
                if (mutex.tryLock()) {
                    try {
                        mapper(event).collect { emit(Unit) }
                    } finally {
                        mutex.unlock()
                    }
                }
            }
        }
    }

    /**
     * Cancels the currently running event task whenever a new event arrives.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun <E> restartable(): EventTransformer<E> = { events, mapper ->
        events.flatMapLatest { mapper(it) }
    }
}
