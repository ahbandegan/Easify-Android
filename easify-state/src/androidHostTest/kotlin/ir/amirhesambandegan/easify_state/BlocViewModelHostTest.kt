package ir.amirhesambandegan.easify_state

import android.content.Context
import android.content.ContextWrapper
import ir.amirhesambandegan.easify_state.android.BlocViewModel
import ir.amirhesambandegan.easify_state.android.findViewModelStoreOwner
import ir.amirhesambandegan.easify_state.core.BlocEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

sealed interface CounterEffect : BlocEffect {
    data class ShowToast(val message: String) : CounterEffect
}

class CounterViewModel(scope: CoroutineScope? = null) :
    BlocViewModel<CounterEvent, CounterState, CounterEffect>(CounterState(0), scope) {
    init {
        on<CounterEvent.Increment> { _, emit ->
            emit(state.value.copy(count = state.value.count + 1))
            postEffect(CounterEffect.ShowToast("Incremented!"))
        }
    }
}

class BlocViewModelHostTest {

    @Test
    fun testBlocViewModelEventAndEffect() = runBlocking {
        val scope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
        val vm = CounterViewModel(scope)

        assertEquals(0, vm.state.value.count)
        vm.add(CounterEvent.Increment)

        val settled = vm.state.first { it.count == 1 }
        assertEquals(1, settled.count)
        assertEquals(CounterEffect.ShowToast("Incremented!"), vm.effect.first())

        scope.cancel()
    }

    @Test
    fun findViewModelStoreOwnerTerminatesSafelyOnNullWrapper() {
        val wrapper = object : ContextWrapper(null) {
            override fun getBaseContext(): Context? = null
        }
        assertNull(wrapper.findViewModelStoreOwner())
    }

    @Test
    fun findViewModelStoreOwnerTerminatesSafelyOnCyclicWrapper() {
        class CyclicWrapper : ContextWrapper(null) {
            var next: Context? = null
            override fun getBaseContext(): Context? = next
        }
        val a = CyclicWrapper()
        val b = CyclicWrapper()
        a.next = b
        b.next = a
        assertNull(a.findViewModelStoreOwner())
    }
}
