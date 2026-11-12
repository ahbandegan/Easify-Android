package ir.amirhesambandegan.easify_state.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.amirhesambandegan.easify_state.core.BlocEffect
import ir.amirhesambandegan.easify_state.core.BlocEvent
import ir.amirhesambandegan.easify_state.core.BlocState
import ir.amirhesambandegan.easify_state.core.EffectBloc
import ir.amirhesambandegan.easify_state.core.Emitter
import ir.amirhesambandegan.easify_state.core.StateStreamable
import ir.amirhesambandegan.easify_state.transform.EventTransformer
import ir.amirhesambandegan.easify_state.transform.Transformers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass

/**
 * An Android [ViewModel] integration for [ir.amirhesambandegan.easify_state.core.Bloc].
 * Allows declaring event handlers via `on<T>` directly inside the ViewModel body.
 */
abstract class BlocViewModel<E : BlocEvent, S : BlocState, SE : BlocEffect>(
    initialState: S,
    scope: CoroutineScope? = null
) : ViewModel(), StateStreamable<S> {

    private open inner class ViewModelDelegate(
        delegateInitialState: S,
        effectiveScope: CoroutineScope
    ) : EffectBloc<E, S, SE>(delegateInitialState, effectiveScope) {
        override fun onEvent(event: E) = this@BlocViewModel.onEvent(event)
        override fun onError(error: Throwable) = this@BlocViewModel.onError(error)
        override fun onUnhandledEvent(event: E) = this@BlocViewModel.onUnhandledEvent(event)

        fun <T : E> bindHandler(
            clazz: KClass<T>,
            transformer: EventTransformer<T>,
            handler: suspend (event: T, emit: Emitter<S>) -> Unit
        ) {
            registerEventHandler(clazz, transformer, handler)
        }
    }

    private val delegate = ViewModelDelegate(initialState, scope ?: viewModelScope)

    override val state: StateFlow<S> = delegate.state
    val effect: Flow<SE> = delegate.effect

    /**
     * Backward-compatible alias for [effect].
     */
    val sideEffect: Flow<SE> get() = effect

    /**
     * Registers an event handler for events of type [T].
     */
    protected inline fun <reified T : E> on(
        noinline transformer: EventTransformer<T> = Transformers.sequential(),
        noinline handler: suspend (event: T, emit: Emitter<S>) -> Unit
    ) {
        registerViewModelHandler(T::class, transformer, handler)
    }

    @PublishedApi
    internal fun <T : E> registerViewModelHandler(
        clazz: KClass<T>,
        transformer: EventTransformer<T>,
        handler: suspend (event: T, emit: Emitter<S>) -> Unit
    ) {
        delegate.bindHandler(clazz, transformer, handler)
    }

    /**
     * Dispatches an [event] to this Bloc.
     */
    fun add(event: E) = delegate.add(event)

    /**
     * Posts a one-off [effect] (e.g. Navigation, Toast, Snackbar).
     */
    protected suspend fun postEffect(effect: SE) {
        delegate.postEffect(effect)
    }

    /**
     * Backward-compatible alias for [postEffect].
     */
    protected suspend fun postSideEffect(effect: SE) {
        postEffect(effect)
    }

    protected open fun onEvent(event: E) {}

    protected open fun onError(error: Throwable) {}

    protected open fun onUnhandledEvent(event: E) {}

    override fun onCleared() {
        super.onCleared()
        delegate.close()
    }
}
