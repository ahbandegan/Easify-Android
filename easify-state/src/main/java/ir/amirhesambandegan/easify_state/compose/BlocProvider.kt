package ir.amirhesambandegan.easify_state.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import ir.amirhesambandegan.easify_state.core.Bloc
import ir.amirhesambandegan.easify_state.core.Cubit
import kotlin.reflect.KClass

val LocalBlocStore = compositionLocalOf<Map<KClass<*>, Any>> { emptyMap() }

/**
 * Retrieves an instance from [LocalBlocStore].
 * Non-inline to avoid Compose IR inlining issues with CompositionLocal.current.
 */
@Composable
fun getBlocFromStore(clazz: KClass<*>): Any? {
    return LocalBlocStore.current[clazz]
}

/**
 * Provides an existing [Bloc] or [Cubit] down the widget tree using Compose [CompositionLocalProvider].
 *
 * @param bloc the instance to provide.
 * @param content child composables with access to [bloc].
 */
@Composable
fun <B : Any> BlocProvider(
    bloc: B,
    content: @Composable () -> Unit
) {
    val parentStore = LocalBlocStore.current
    val newStore = remember(parentStore, bloc) {
        parentStore + (bloc::class to bloc)
    }
    CompositionLocalProvider(LocalBlocStore provides newStore) {
        content()
    }
}

/**
 * Creates and provides a [Bloc] or [Cubit] to its children.
 * Automatically closes the [Bloc] or [Cubit] when leaving the composition.
 *
 * @param create factory lambda creating the instance.
 * @param content child composables with access to the created instance.
 */
@Composable
inline fun <reified B : Any> BlocProvider(
    crossinline create: () -> B,
    noinline content: @Composable () -> Unit
) {
    val bloc = remember { create() }
    DisposableEffect(bloc) {
        onDispose {
            when (bloc) {
                is Bloc<*, *> -> bloc.close()
                is Cubit<*> -> bloc.close()
            }
        }
    }
    BlocProvider(bloc = bloc, content = content)
}
