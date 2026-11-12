package ir.amirhesambandegan.easify_state.compose

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

@Composable
@PublishedApi
internal actual fun resolvePlatformBloc(clazz: KClass<*>): Any? = null
