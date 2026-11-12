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
fun getBlocFromStore(clazz: KClass<*>): Any? = LocalBlocStore.current[clazz]

@Composable
@PublishedApi
internal expect fun resolvePlatformBloc(clazz: KClass<*>): Any?

/**
 * Provides an existing [Bloc], [Cubit], or any state holder instance down the composable tree.
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
 * Automatically closes the [Bloc] or [Cubit] when leaving composition.
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

/**
 * Provides multiple pre-existing [Bloc] or [Cubit] instances at once without nested providers.
 */
@Composable
fun MultiBlocProvider(
    vararg blocs: Any,
    content: @Composable () -> Unit
) {
    val parentStore = LocalBlocStore.current
    val blocList = blocs.toList()
    val newStore = remember(parentStore, blocList) {
        buildMap(parentStore.size + blocList.size) {
            putAll(parentStore)
            blocList.forEach { instance -> put(instance::class, instance) }
        }
    }
    CompositionLocalProvider(LocalBlocStore provides newStore) {
        content()
    }
}
