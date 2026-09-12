package ir.amirhesambandegan.easify_state.compose

import androidx.compose.runtime.Composable
import ir.amirhesambandegan.easify_state.core.Bloc
import ir.amirhesambandegan.easify_state.core.BlocEffect
import ir.amirhesambandegan.easify_state.core.BlocState
import ir.amirhesambandegan.easify_state.core.EffectBloc

/**
 * Combines [BlocListener] and [BlocBuilder] into a single composable.
 * Mirroring Flutter's `BlocConsumer`.
 */
@Composable
fun <B : Bloc<*, S>, S : BlocState> BlocConsumer(
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
 * Combines [BlocEffectListener] and [BlocBuilder] for Blocs with effects.
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
