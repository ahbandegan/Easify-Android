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
 * Rebuilds its content in response to state changes from [stateFlow].
 */
@Composable
fun <S : BlocState> BlocBuilder(
    stateFlow: StateFlow<S>,
    buildWhen: ((previous: S, current: S) -> Boolean)? = null,
    content: @Composable (state: S) -> Unit
) {
    val currentState = stateFlow.collectAsStateWithLifecycle().value
    val renderedStateHolder = remember(stateFlow) { mutableStateOf(currentState) }
    val currentBuildWhen = rememberUpdatedState(buildWhen)

    if (currentState != renderedStateHolder.value) {
        val shouldRebuild = currentBuildWhen.value?.invoke(renderedStateHolder.value, currentState) ?: true
        if (shouldRebuild) {
            renderedStateHolder.value = currentState
        }
    }

    content(renderedStateHolder.value)
}

/**
 * Rebuilds its content in response to state changes from any [StateStreamable].
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
