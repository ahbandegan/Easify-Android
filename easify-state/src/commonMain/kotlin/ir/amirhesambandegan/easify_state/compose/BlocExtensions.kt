package ir.amirhesambandegan.easify_state.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.amirhesambandegan.easify_state.core.BlocState
import ir.amirhesambandegan.easify_state.core.StateStreamable

/**
 * Reads an instance of [B] from [LocalBlocStore] or platform ViewModel store (on Android).
 */
@Composable
inline fun <reified B : Any> read(): B {
    val stored = getBlocFromStore(B::class) as? B
    if (stored != null) return stored

    val platform = resolvePlatformBloc(B::class) as? B
    if (platform != null) return platform

    error("No instance of ${B::class.simpleName} found in BlocProvider or platform store.")
}

/**
 * Watches any [StateStreamable] (`Bloc`, `Cubit`, `BlocViewModel`), returning its current state
 * and recomposing whenever state changes.
 */
@Composable
inline fun <reified B : StateStreamable<S>, S : BlocState> watch(): S {
    val streamable = read<B>()
    return streamable.state.collectAsStateWithLifecycle().value
}

/**
 * Subscribes to a derived slice of [StateStreamable] state via [selector],
 * recomposing only when the selected value changes.
 */
@Composable
inline fun <reified B : StateStreamable<S>, S : BlocState, R> select(
    noinline selector: (S) -> R
): R {
    val streamable = read<B>()
    val state by streamable.state.collectAsStateWithLifecycle()
    val currentSelector by rememberUpdatedState(selector)
    val selectedValue by remember(streamable) {
        derivedStateOf { currentSelector(state) }
    }
    return selectedValue
}
