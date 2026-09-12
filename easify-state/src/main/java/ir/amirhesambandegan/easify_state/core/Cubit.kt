package ir.amirhesambandegan.easify_state.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive

/**
 * A [Cubit] is a subset of [Bloc] that relies on explicit functions rather than events to emit state.
 * Direct equivalent of Flutter's `Cubit`.
 */
abstract class Cubit<S : BlocState>(
    initialState: S,
    protected val scope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
) : StateStreamable<S> {
    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<S> = _state.asStateFlow()

    var isClosed: Boolean = false
        private set

    init {
        BlocObserver.current?.onCreate(this)
    }

    /**
     * Updates the state to [nextState] if it differs from the current state.
     */
    protected fun emit(nextState: S) {
        if (isClosed || !scope.isActive) return
        if (_state.value != nextState) {
            val change = Change(_state.value, nextState)
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
