package ir.amirhesambandegan.easify_state.android

import androidx.compose.runtime.Composable
import ir.amirhesambandegan.easify_state.compose.BlocBuilder
import ir.amirhesambandegan.easify_state.compose.BlocEffectListener
import ir.amirhesambandegan.easify_state.core.BlocEffect
import ir.amirhesambandegan.easify_state.core.BlocState

/**
 * Listens to effects emitted by a [BlocViewModel].
 */
@Composable
fun <SE : BlocEffect> BlocEffectListener(
    viewModel: BlocViewModel<*, *, SE>,
    onEffect: (SE) -> Unit
) {
    BlocEffectListener(effectFlow = viewModel.effect, onEffect = onEffect)
}

/**
 * Combines [BlocEffectListener] and [BlocBuilder] for a [BlocViewModel].
 */
@Composable
fun <S : BlocState, SE : BlocEffect> BlocEffectConsumer(
    viewModel: BlocViewModel<*, S, SE>,
    onEffect: (SE) -> Unit,
    buildWhen: ((previous: S, current: S) -> Boolean)? = null,
    content: @Composable (state: S) -> Unit
) {
    BlocEffectListener(viewModel = viewModel, onEffect = onEffect)
    BlocBuilder(bloc = viewModel, buildWhen = buildWhen, content = content)
}
