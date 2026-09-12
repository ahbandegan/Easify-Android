package ir.amirhesambandegan.easify_state.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.amirhesambandegan.easify_state.android.readBloc
import ir.amirhesambandegan.easify_state.core.BlocState
import ir.amirhesambandegan.easify_state.core.StateStreamable

/**
 * Reads an instance of [B] from [LocalBlocStore] or [LocalContext].
 * Direct Composable shorthand for `context.read<T>()`.
 */
@Composable
inline fun <reified B : Any> read(): B {
    val context = LocalContext.current
    return context.readBloc<B>()
}

/**
 * Watches any [StateStreamable] (Bloc, Cubit, BlocViewModel), returning its state
 * and triggering recomposition whenever the state changes.
 * Direct Composable shorthand for `context.watch<T>()`.
 */
@Composable
inline fun <reified B : StateStreamable<S>, S : BlocState> watch(): S {
    val streamable = read<B>()
    return streamable.state.collectAsStateWithLifecycle().value
}

/**
 * Subscribes to a subset of [StateStreamable] state via [selector].
 * Recomposes only when the selected value changes.
 * Direct Composable shorthand for `context.select<T, R>()`.
 */
@Composable
inline fun <reified B : StateStreamable<S>, S : BlocState, R> select(
    crossinline selector: (S) -> R
): R {
    val streamable = read<B>()
    val state by streamable.state.collectAsStateWithLifecycle()
    val selectedValue by remember(streamable) {
        derivedStateOf { selector(state) }
    }
    return selectedValue
}
