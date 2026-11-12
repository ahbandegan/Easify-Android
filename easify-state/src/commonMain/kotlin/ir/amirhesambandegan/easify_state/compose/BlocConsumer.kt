package ir.amirhesambandegan.easify_state.compose

import androidx.compose.runtime.Composable
import ir.amirhesambandegan.easify_state.core.BlocEffect
import ir.amirhesambandegan.easify_state.core.BlocState
import ir.amirhesambandegan.easify_state.core.EffectBloc
import ir.amirhesambandegan.easify_state.core.StateStreamable

/**
 * Combines [BlocListener] and [BlocBuilder] into a single composable for any [StateStreamable].
 */
@Composable
fun <B : StateStreamable<S>, S : BlocState> BlocConsumer(
    bloc: B,
    onState: (state: S) -> Unit,
    listenWhen: ((previous: S, current: S) -> Boolean)? = null,
    buildWhen: ((previous: S, current: S) -> Boolean)? = null,
    content: @Composable (state: S) -> Unit
) {
    BlocListener(bloc = bloc, listenWhen = listenWhen, onState = onState)
    BlocBuilder(bloc = bloc, buildWhen = buildWhen, content = content)
}

/**
 * Combines [BlocListener] and [BlocBuilder] using a [StateStreamable] retrieved via [read].
 */
@Composable
inline fun <reified B : StateStreamable<S>, S : BlocState> BlocConsumer(
    noinline onState: (state: S) -> Unit,
    noinline listenWhen: ((previous: S, current: S) -> Boolean)? = null,
    noinline buildWhen: ((previous: S, current: S) -> Boolean)? = null,
    noinline content: @Composable (state: S) -> Unit
) {
    val streamable = read<B>()
    BlocConsumer(
        bloc = streamable,
        onState = onState,
        listenWhen = listenWhen,
        buildWhen = buildWhen,
        content = content
    )
}

/**
 * Combines [BlocEffectListener] and [BlocBuilder] for [EffectBloc]s.
 */
@Composable
fun <S : BlocState, SE : BlocEffect> BlocEffectConsumer(
    bloc: EffectBloc<*, S, SE>,
    onEffect: (effect: SE) -> Unit,
    buildWhen: ((previous: S, current: S) -> Boolean)? = null,
    content: @Composable (state: S) -> Unit
) {
    BlocEffectListener(bloc = bloc, onEffect = onEffect)
    BlocBuilder(bloc = bloc, buildWhen = buildWhen, content = content)
}
