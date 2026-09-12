package ir.amirhesambandegan.easify_state.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import ir.amirhesambandegan.easify_state.core.BlocState
import ir.amirhesambandegan.easify_state.core.StateStreamable
import kotlinx.coroutines.flow.StateFlow

/**
 * Invokes [onState] in response to state changes.
 * Mirroring Flutter's `BlocListener`.
 *
 * @param stateFlow the source of state.
 * @param listenWhen optional condition deciding whether [onState] should be invoked.
 * @param onState callback to execute on state changes (e.g. Navigation, Dialog, Analytics).
 */
@Composable
fun <S : BlocState> BlocListener(
    stateFlow: StateFlow<S>,
    listenWhen: ((previous: S, current: S) -> Boolean)? = null,
    onState: (state: S) -> Unit
) {
    val currentListener = rememberUpdatedState(onState)
    val currentListenWhen = rememberUpdatedState(listenWhen)
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(stateFlow, lifecycleOwner) {
        var previousState = stateFlow.value
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            stateFlow.collect { currentState ->
                val shouldCall = currentListenWhen.value?.invoke(previousState, currentState) ?: true
                if (shouldCall) {
                    currentListener.value(currentState)
                }
                previousState = currentState
            }
        }
    }
}

/**
 * Listens to state changes from any [StateStreamable] (Bloc, Cubit, BlocViewModel).
 */
@Composable
fun <S : BlocState> BlocListener(
    bloc: StateStreamable<S>,
    listenWhen: ((previous: S, current: S) -> Boolean)? = null,
    onState: (state: S) -> Unit
) {
    BlocListener(stateFlow = bloc.state, listenWhen = listenWhen, onState = onState)
}

/**
 * Listens to state changes from a [StateStreamable] retrieved via [read].
 */
@Composable
inline fun <reified B : StateStreamable<S>, S : BlocState> BlocListener(
    noinline listenWhen: ((previous: S, current: S) -> Boolean)? = null,
    noinline onState: (state: S) -> Unit
) {
    val streamable = read<B>()
    BlocListener(bloc = streamable, listenWhen = listenWhen, onState = onState)
}
