package ir.amirhesambandegan.easify_state.core

import ir.amirhesambandegan.easify_state.transform.EventTransformer
import ir.amirhesambandegan.easify_state.transform.Transformers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.reflect.KClass

/**
 * Multiplatform [Bloc] managing state transitions based on incoming [BlocEvent]s.
 *
 * Uses per-handler buffered channels so events added immediately after instantiation are
 * never dropped regardless of coroutine dispatcher scheduling.
 */
@OptIn(ExperimentalAtomicApi::class)
abstract class Bloc<E : BlocEvent, S : BlocState>(
    initialState: S,
    protected val scope: CoroutineScope = CoroutineScope(createDefaultBlocDispatcher() + SupervisorJob())
) : StateStreamable<S> {

    private class HandlerEntry<E : BlocEvent>(
        val eventClass: KClass<out E>,
        val dispatch: (E) -> Unit,
        val closeChannel: () -> Unit
    )

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<S> = _state.asStateFlow()

    private val handlersRef = AtomicReference<List<HandlerEntry<E>>>(emptyList())

    @Volatile
    var isClosed: Boolean = false
        private set

    init {
        BlocObserver.current?.onCreate(this)
    }

    /**
     * Registers an event handler for events of type [T].
     */
    protected inline fun <reified T : E> on(
        noinline transformer: EventTransformer<T> = Transformers.sequential(),
        noinline handler: suspend (event: T, emit: Emitter<S>) -> Unit
    ) {
        registerHandler(T::class, transformer, handler)
    }

    /**
     * Non-inline bridge allowing cross-module [Bloc] / [EffectBloc] wrappers (such as Android
     * `BlocViewModel`) to register typed event handlers without exposing internal handler tables.
     */
    protected fun <T : E> registerEventHandler(
        clazz: KClass<T>,
        transformer: EventTransformer<T> = Transformers.sequential(),
        handler: suspend (event: T, emit: Emitter<S>) -> Unit
    ) {
        registerHandler(clazz, transformer, handler)
    }

    @PublishedApi
    internal fun <T : E> registerHandler(
        clazz: KClass<T>,
        transformer: EventTransformer<T>,
        handler: suspend (event: T, emit: Emitter<S>) -> Unit
    ) {
        val channel = Channel<T>(Channel.UNLIMITED)
        val entry = HandlerEntry<E>(
            eventClass = clazz,
            dispatch = { event ->
                if (clazz.isInstance(event)) {
                    @Suppress("UNCHECKED_CAST")
                    channel.trySend(event as T)
                }
            },
            closeChannel = { channel.close() }
        )

        while (true) {
            val current = handlersRef.load()
            check(current.none { it.eventClass == clazz }) {
                "on<${clazz.simpleName}> was called multiple times. Each event type must only have one handler."
            }
            if (handlersRef.compareAndSet(current, current + entry)) break
        }

        val eventsFlow: Flow<T> = channel.receiveAsFlow()
        scope.launch {
            transformer(eventsFlow) { event ->
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
     * Dispatches an [event] to the registered handlers of this [Bloc].
     */
    open fun add(event: E) {
        if (isClosed || !scope.isActive) return
        BlocObserver.current?.onEvent(this, event)
        onEvent(event)

        val currentHandlers = handlersRef.load()
        var matched = false
        for (entry in currentHandlers) {
            if (entry.eventClass.isInstance(event)) {
                matched = true
                entry.dispatch(event)
            }
        }
        if (!matched) {
            onUnhandledEvent(event)
        }
    }

    private fun emitState(event: E, nextState: S) {
        val currentState = _state.value
        if (currentState != nextState) {
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
     * Closes the [Bloc], closes event channels, and cancels its coroutine scope.
     */
    open fun close() {
        if (isClosed) return
        isClosed = true
        for (entry in handlersRef.load()) {
            entry.closeChannel()
        }
        BlocObserver.current?.onClose(this)
        scope.cancel()
    }
}

/**
 * Multiplatform [Cubit] relying on explicit functions rather than events to emit state.
 */
abstract class Cubit<S : BlocState>(
    initialState: S,
    protected val scope: CoroutineScope = CoroutineScope(createDefaultBlocDispatcher() + SupervisorJob())
) : StateStreamable<S> {
    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<S> = _state.asStateFlow()

    @Volatile
    var isClosed: Boolean = false
        private set

    init {
        BlocObserver.current?.onCreate(this)
    }

    /**
     * Updates state to [nextState] if it differs from the current state.
     */
    protected fun emit(nextState: S) {
        if (isClosed || !scope.isActive) return
        val current = _state.value
        if (current != nextState) {
            val change = Change(current, nextState)
            BlocObserver.current?.onChange(this, change)
            onChange(change)
            _state.value = nextState
        }
    }

    protected open fun onChange(change: Change<S>) {}

    protected open fun onError(error: Throwable) {}

    open fun close() {
        if (isClosed) return
        isClosed = true
        BlocObserver.current?.onClose(this)
        scope.cancel()
    }
}

/**
 * Multiplatform [Bloc] that also supports emitting one-off [BlocEffect]s (Navigation, Toast, Snackbar).
 */
abstract class EffectBloc<E : BlocEvent, S : BlocState, SE : BlocEffect>(
    initialState: S,
    scope: CoroutineScope = CoroutineScope(createDefaultBlocDispatcher() + SupervisorJob())
) : Bloc<E, S>(initialState, scope) {

    private val _effect = Channel<SE>(Channel.BUFFERED)
    val effect: Flow<SE> = _effect.receiveAsFlow()

    suspend fun postEffect(effect: SE) {
        if (!isClosed) {
            _effect.send(effect)
        }
    }

    override fun close() {
        _effect.close()
        super.close()
    }
}
