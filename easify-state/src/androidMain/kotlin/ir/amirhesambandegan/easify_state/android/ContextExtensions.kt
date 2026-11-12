package ir.amirhesambandegan.easify_state.android

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.amirhesambandegan.easify_state.compose.getBlocFromStore
import ir.amirhesambandegan.easify_state.core.BlocState
import ir.amirhesambandegan.easify_state.core.StateStreamable
import java.util.Collections
import java.util.IdentityHashMap

/**
 * Traverses the [ContextWrapper] chain to find the nearest [ViewModelStoreOwner],
 * guarding against cyclic [ContextWrapper] chains.
 */
fun Context.findViewModelStoreOwner(): ViewModelStoreOwner? {
    val visited = Collections.newSetFromMap(IdentityHashMap<Context, Boolean>())
    var current: Context? = this
    while (current != null && visited.add(current)) {
        when (current) {
            is ViewModelStoreOwner -> return current
            is ContextWrapper -> current = current.baseContext
            else -> return null
        }
    }
    return null
}

/**
 * Reads a [ViewModel] (such as [BlocViewModel]) from the [Context] without subscribing to state changes.
 */
inline fun <reified B : ViewModel> Context.read(): B {
    val owner = findViewModelStoreOwner()
        ?: error("Context $this is not a ViewModelStoreOwner, nor is any enclosing ContextWrapper.")
    return ViewModelProvider(owner)[B::class.java]
}

/**
 * In a Composable context, reads an instance of [B] from either `LocalBlocStore`
 * or the nearest [ViewModelStoreOwner].
 */
@Composable
inline fun <reified B : Any> Context.readBloc(): B {
    val found = getBlocFromStore(B::class) as? B
    if (found != null) return found

    if (ViewModel::class.java.isAssignableFrom(B::class.java)) {
        val owner = findViewModelStoreOwner()
        if (owner != null) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelProvider(owner)[B::class.java as Class<ViewModel>] as B
        }
    }

    error("No instance of ${B::class.simpleName} found in BlocProvider or ViewModelStoreOwner.")
}

/**
 * Watches a [StateStreamable] in a Composable context, collecting its state with lifecycle.
 */
@Composable
inline fun <reified B : StateStreamable<S>, S : BlocState> Context.watch(): S {
    val streamable = readBloc<B>()
    return streamable.state.collectAsStateWithLifecycle().value
}
