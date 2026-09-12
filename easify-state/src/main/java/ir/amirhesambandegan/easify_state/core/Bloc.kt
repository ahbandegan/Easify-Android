package ir.amirhesambandegan.easify_state.core

import ir.amirhesambandegan.easify_state.transform.EventTransformer
import ir.amirhesambandegan.easify_state.transform.Transformers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.reflect.KClass

/**
 * A [Bloc] manages state changes based on incoming [BlocEvent]s.
 * It is modeled directly after Flutter's `bloc` library.
 */
abstract class Bloc<E : BlocEvent, S : BlocState>(
    initialState: S,
    protected val scope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
) : StateStreamable<S> {
    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<S> = _state.asStateFlow()

    private val incomingEvents = MutableSharedFlow<E>(extraBufferCapacity = 64)
    private val registeredTypes = CopyOnWriteArraySet<KClass<out E>>()

    var isClosed: Boolean = false
        private set

    init {
        BlocObserver.current?.onCreate(this)
    }

    /**
     * Registers an event handler for events of type [T].
     *
     * @param transformer defines how events of type [T] are processed.
     * Defaults to [Transformers.sequential].
     * @param handler the suspend function that handles the event and emits new states.
     */
    protected inline fun <reified T : E> on(
        noinline transformer: EventTransformer<T> = Transformers.sequential(),
        noinline handler: suspend (event: T, emit: Emitter<S>) -> Unit
    ) {
        registerHandler(T::class, transformer, handler)
    }

    @PublishedApi
    internal fun <T : E> registerHandler(
        clazz: KClass<T>,
        transformer: EventTransformer<T>,
        handler: suspend (event: T, emit: Emitter<S>) -> Unit
    ) {
        check(!registeredTypes.contains(clazz)) {
            "on<${clazz.simpleName}> was called multiple times. Each event type must only have one handler."
        }
        registeredTypes.add(clazz)

        val filteredEvents: Flow<T> = incomingEvents.filterIsInstance(clazz)

        scope.launch {
            transformer(filteredEvents) { event ->
                flow {
                    val emitter = object : Emitter<S> {
                        override val isDone: Boolean
                            get() = isClosed || !scope.isActive

                        override suspend fun emit(state: S) {
                            if (isDone) return
                            emitState(event, state)
                        }
                    }

                    try {
                        handler(event, emitter)
                    } catch (e: CancellationException) {
                        throw e
                    } catch (t: Throwable) {
                        BlocObserver.current?.onError(this@Bloc, t)
                        onError(t)
                    }
                    emit(Unit)
                }
            }.collect()
        }
    }

    /**
     * Dispatches an [event] to the [Bloc].
     */
    open fun add(event: E) {
        if (isClosed || !scope.isActive) return
        BlocObserver.current?.onEvent(this, event)
        onEvent(event)

        val hasHandler = registeredTypes.any { it.java.isInstance(event) }
        if (!hasHandler) {
            onUnhandledEvent(event)
        }

        if (!incomingEvents.tryEmit(event)) {
            scope.launch {
                incomingEvents.emit(event)
            }
        }
    }

    private fun emitState(event: E, nextState: S) {
        if (_state.value != nextState) {
            val currentState = _state.value
            val transition = Transition(currentState, event, nextState)
            val change = transition.toChange()

            BlocObserver.current?.onTransition(this, transition)
            BlocObserver.current?.onChange(this, change)
            onTransition(transition)
            onChange(change)

            _state.value = nextState
        }
    }

    protected open fun onEvent(event: E) {}

    protected open fun onChange(change: Change<S>) {}

    protected open fun onTransition(transition: Transition<E, S>) {}

    protected open fun onError(error: Throwable) {}

    protected open fun onUnhandledEvent(event: E) {}

    /**
     * Closes the [Bloc] and cancels its underlying coroutine scope.
     */
    open fun close() {
        if (isClosed) return
        isClosed = true
        BlocObserver.current?.onClose(this)
        scope.cancel()
    }
}
