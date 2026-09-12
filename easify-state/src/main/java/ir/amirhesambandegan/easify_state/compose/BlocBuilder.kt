package ir.amirhesambandegan.easify_state.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.amirhesambandegan.easify_state.core.BlocState
import ir.amirhesambandegan.easify_state.core.StateStreamable
import kotlinx.coroutines.flow.StateFlow

/**
 * Rebuilds its content in response to state changes.
 *
 * @param stateFlow the source of state.
 * @param buildWhen optional condition taking previous and current states to decide if content should rebuild.
 * @param content the composable UI rendering the state.
 */
@Composable
fun <S : BlocState> BlocBuilder(
    stateFlow: StateFlow<S>,
    buildWhen: ((previous: S, current: S) -> Boolean)? = null,
    content: @Composable (state: S) -> Unit
) {
    val currentState = stateFlow.collectAsStateWithLifecycle().value
    val renderedState = remember(stateFlow) { mutableStateOf(currentState) }

    val currentBuildWhen = rememberUpdatedState(buildWhen)
    val shouldRebuild = currentBuildWhen.value?.invoke(renderedState.value, currentState) ?: true

    if (shouldRebuild) {
        renderedState.value = currentState
    }

    content(renderedState.value)
}

/**
 * Rebuilds its content in response to state changes from any [StateStreamable] (Bloc, Cubit, BlocViewModel).
 */
@Composable
fun <S : BlocState> BlocBuilder(
    bloc: StateStreamable<S>,
    buildWhen: ((previous: S, current: S) -> Boolean)? = null,
    content: @Composable (state: S) -> Unit
) {
    BlocBuilder(stateFlow = bloc.state, buildWhen = buildWhen, content = content)
}

/**
 * Rebuilds its content in response to state changes from a [StateStreamable] retrieved via [read].
 */
@Composable
inline fun <reified B : StateStreamable<S>, S : BlocState> BlocBuilder(
    noinline buildWhen: ((previous: S, current: S) -> Boolean)? = null,
    noinline content: @Composable (state: S) -> Unit
) {
    val streamable = read<B>()
    BlocBuilder(bloc = streamable, buildWhen = buildWhen, content = content)
}
