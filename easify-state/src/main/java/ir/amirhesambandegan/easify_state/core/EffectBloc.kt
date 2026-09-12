package ir.amirhesambandegan.easify_state.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * A [Bloc] that also supports emitting one-off [BlocEffect]s (such as Navigation, Toast, Snackbar).
 */
abstract class EffectBloc<E : BlocEvent, S : BlocState, SE : BlocEffect>(
    initialState: S,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
) : Bloc<E, S>(initialState, scope) {

    private val _effect = Channel<SE>(Channel.BUFFERED)
    val effect: Flow<SE> = _effect.receiveAsFlow()

    suspend fun postEffect(effect: SE) {
        if (!isClosed) {
            _effect.send(effect)
        }
    }

    override fun close() {
        _effect.close()
        super.close()
    }
}
