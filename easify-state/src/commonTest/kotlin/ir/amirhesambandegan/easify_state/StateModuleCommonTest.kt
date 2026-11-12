package ir.amirhesambandegan.easify_state

import ir.amirhesambandegan.easify_state.core.Bloc
import ir.amirhesambandegan.easify_state.core.BlocEvent
import ir.amirhesambandegan.easify_state.core.BlocState
import ir.amirhesambandegan.easify_state.core.Cubit
import ir.amirhesambandegan.easify_state.transform.Transformers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

data class CounterState(val count: Int) : BlocState

sealed interface CounterEvent : BlocEvent {
    data object Increment : CounterEvent
    data class Add(val amount: Int) : CounterEvent
}

class CounterBloc(scope: CoroutineScope) : Bloc<CounterEvent, CounterState>(CounterState(0), scope) {
    init {
        on<CounterEvent.Increment> { _, emit ->
            emit(state.value.copy(count = state.value.count + 1))
        }
        on<CounterEvent.Add>(transformer = Transformers.sequential()) { event, emit ->
            emit(state.value.copy(count = state.value.count + event.amount))
        }
    }
}

class CounterCubit(scope: CoroutineScope) : Cubit<CounterState>(CounterState(0), scope) {
    fun increment() {
        emit(state.value.copy(count = state.value.count + 1))
    }
}

class StateModuleCommonTest {

    @Test
    fun blocHandlesSequentialEvents() = runTest {
        val scope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
        val bloc = CounterBloc(scope)

        assertEquals(0, bloc.state.value.count)

        bloc.add(CounterEvent.Increment)
        bloc.add(CounterEvent.Add(5))

        val settled = bloc.state.first { it.count == 6 }
        assertEquals(6, settled.count)

        bloc.close()
        scope.cancel()
    }

    @Test
    fun cubitUpdatesState() = runTest {
        val scope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
        val cubit = CounterCubit(scope)

        cubit.increment()
        cubit.increment()
        assertEquals(2, cubit.state.value.count)

        cubit.close()
        scope.cancel()
    }
}
