package ir.amirhesambandegan.easify_state

import ir.amirhesambandegan.easify_state.core.Bloc
import ir.amirhesambandegan.easify_state.core.BlocEffect
import ir.amirhesambandegan.easify_state.core.BlocEvent
import ir.amirhesambandegan.easify_state.core.BlocObserver
import ir.amirhesambandegan.easify_state.core.BlocState
import ir.amirhesambandegan.easify_state.core.Cubit
import ir.amirhesambandegan.easify_state.core.EffectBloc
import ir.amirhesambandegan.easify_state.core.LoggingBlocObserver
import ir.amirhesambandegan.easify_state.transform.Transformers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

data class CoreCounterState(val count: Int) : BlocState

sealed interface CoreCounterEvent : BlocEvent {
    data object Increment : CoreCounterEvent
    data class Add(val amount: Int) : CoreCounterEvent
}

sealed interface CoreCounterEffect : BlocEffect {
    data class Toast(val message: String) : CoreCounterEffect
}

class CoreCounterBloc(scope: CoroutineScope) :
    Bloc<CoreCounterEvent, CoreCounterState>(CoreCounterState(0), scope) {
    init {
        on<CoreCounterEvent.Increment> { _, emit ->
            emit(state.value.copy(count = state.value.count + 1))
        }
        on<CoreCounterEvent.Add>(transformer = Transformers.sequential()) { event, emit ->
            emit(state.value.copy(count = state.value.count + event.amount))
        }
    }
}

class ConsoleDefaultBloc : Bloc<CoreCounterEvent, CoreCounterState>(CoreCounterState(0)) {
    init {
        on<CoreCounterEvent.Add> { event, emit ->
            emit(state.value.copy(count = state.value.count + event.amount))
        }
    }
}

class CoreCounterCubit(scope: CoroutineScope) : Cubit<CoreCounterState>(CoreCounterState(0), scope) {
    fun increment() = emit(state.value.copy(count = state.value.count + 1))
}

class CoreEffectBloc(scope: CoroutineScope) :
    EffectBloc<CoreCounterEvent, CoreCounterState, CoreCounterEffect>(CoreCounterState(0), scope) {
    init {
        on<CoreCounterEvent.Increment> { _, emit ->
            emit(state.value.copy(count = state.value.count + 1))
            postEffect(CoreCounterEffect.Toast("Done"))
        }
    }
}

class BlocCoreTest {

    @Test
    fun blocHandlesSequentialAndUnconfinedEvents() = runTest {
        val scope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
        val bloc = CoreCounterBloc(scope)

        assertEquals(0, bloc.state.value.count)
        bloc.add(CoreCounterEvent.Increment)
        assertEquals(1, bloc.state.value.count)
        bloc.add(CoreCounterEvent.Add(5))
        assertEquals(6, bloc.state.value.count)

        bloc.close()
        scope.cancel()
    }

    @Test
    fun blocDoesNotDropEarlyEventsOnDefaultDispatcher() = runTest {
        val bloc = ConsoleDefaultBloc()
        repeat(10) {
            bloc.add(CoreCounterEvent.Add(1))
        }
        val finalState = bloc.state.first { it.count == 10 }
        assertEquals(10, finalState.count)
        bloc.close()
    }

    @Test
    fun cubitAndEffectBlocAndLoggingObserverWork() = runTest {
        val logs = mutableListOf<String>()
        val previousObserver = BlocObserver.current
        BlocObserver.current = LoggingBlocObserver(logger = { logs += it })
        try {
            val scope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
            val cubit = CoreCounterCubit(scope)
            cubit.increment()
            assertEquals(1, cubit.state.value.count)

            val effectBloc = CoreEffectBloc(scope)
            effectBloc.add(CoreCounterEvent.Increment)
            assertEquals(1, effectBloc.state.value.count)
            assertEquals(CoreCounterEffect.Toast("Done"), effectBloc.effect.first())

            assertTrue(logs.any { it.contains("onTransition") })
            assertTrue(logs.any { it.contains("onChange") })

            cubit.close()
            effectBloc.close()
            scope.cancel()
        } finally {
            BlocObserver.current = previousObserver
        }
    }
}
