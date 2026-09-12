package ir.amirhesambandegan.easify_state.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.amirhesambandegan.easify_state.core.BlocState
import ir.amirhesambandegan.easify_state.core.StateStreamable
import kotlinx.coroutines.flow.StateFlow

/**
 * Rebuilds its content only when the selected value [T] of [stateFlow] changes.
 * Mirroring Flutter's `BlocSelector`.
 *
 * @param stateFlow source of state.
 * @param selector mapping function extracting value [T] from state [S].
 * @param content composable UI receiving the selected value [T].
 */
@Composable
fun <S : BlocState, T> BlocSelector(
    stateFlow: StateFlow<S>,
    selector: (S) -> T,
    content: @Composable (selected: T) -> Unit
) {
    val state by stateFlow.collectAsStateWithLifecycle()
    val selected by remember(stateFlow) {
        derivedStateOf { selector(state) }
    }
    content(selected)
}

/**
 * Rebuilds its content only when the selected value [T] of any [StateStreamable] changes.
 */
@Composable
fun <S : BlocState, T> BlocSelector(
    bloc: StateStreamable<S>,
    selector: (S) -> T,
    content: @Composable (selected: T) -> Unit
) {
    BlocSelector(stateFlow = bloc.state, selector = selector, content = content)
}

/**
 * Rebuilds its content only when the selected value [T] of a [StateStreamable] retrieved via [read] changes.
 */
@Composable
inline fun <reified B : StateStreamable<S>, S : BlocState, T> BlocSelector(
    noinline selector: (S) -> T,
    noinline content: @Composable (selected: T) -> Unit
) {
    val streamable = read<B>()
    BlocSelector(bloc = streamable, selector = selector, content = content)
}
