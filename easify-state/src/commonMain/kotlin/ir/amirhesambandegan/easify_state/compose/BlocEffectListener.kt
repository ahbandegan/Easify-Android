package ir.amirhesambandegan.easify_state.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import ir.amirhesambandegan.easify_state.core.BlocEffect
import ir.amirhesambandegan.easify_state.core.EffectBloc
import kotlinx.coroutines.flow.Flow

/**
 * Listens to one-off [BlocEffect]s with lifecycle-safety and stale-lambda protection.
 */
@Composable
fun <SE : BlocEffect> BlocEffectListener(
    effectFlow: Flow<SE>,
    onEffect: (SE) -> Unit
) {
    val currentListener = rememberUpdatedState(onEffect)
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(effectFlow, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            effectFlow.collect { effect ->
                currentListener.value(effect)
            }
        }
    }
}

/**
 * Listens to effects emitted by an [EffectBloc].
 */
@Composable
fun <SE : BlocEffect> BlocEffectListener(
    bloc: EffectBloc<*, *, SE>,
    onEffect: (SE) -> Unit
) {
    BlocEffectListener(effectFlow = bloc.effect, onEffect = onEffect)
}
