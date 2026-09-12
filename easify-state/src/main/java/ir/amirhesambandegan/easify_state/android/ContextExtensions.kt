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

/**
 * Traverses the [ContextWrapper] chain to find the nearest [ViewModelStoreOwner].
 */
tailrec fun Context.findViewModelStoreOwner(): ViewModelStoreOwner? = when (this) {
    is ViewModelStoreOwner -> this
    is ContextWrapper -> baseContext.findViewModelStoreOwner()
    else -> null
}

/**
 * Reads a [ViewModel] (such as [BlocViewModel]) from the [Context] without subscribing to state changes.
 * Mirroring Flutter's `context.read<T>()`.
 */
inline fun <reified B : ViewModel> Context.read(): B {
    val owner = findViewModelStoreOwner()
        ?: error("Context $this is not a ViewModelStoreOwner, nor is any enclosing ContextWrapper.")
    return ViewModelProvider(owner)[B::class.java]
}

/**
 * In a Composable context, reads an instance of [B] from either [ir.amirhesambandegan.easify_state.compose.LocalBlocStore] (if provided via BlocProvider)
 * or the nearest [ViewModelStoreOwner].
 * Mirroring Flutter's `context.read<T>()`.
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

    error("No instance of ${B::class.qualifiedName} found in BlocProvider or ViewModelStoreOwner.")
}

/**
 * Watches a [StateStreamable] (such as [ir.amirhesambandegan.easify_state.core.Bloc], [BlocViewModel], or [ir.amirhesambandegan.easify_state.core.Cubit])
 * in a Composable context, collecting its state with lifecycle and triggering recomposition.
 * Mirroring Flutter's `context.watch<T>()`.
 */
@Composable
inline fun <reified B : StateStreamable<S>, S : BlocState> Context.watch(): S {
    val streamable = readBloc<B>()
    return streamable.state.collectAsStateWithLifecycle().value
}
