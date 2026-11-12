package ir.amirhesambandegan.easify_state.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.amirhesambandegan.easify_state.android.findViewModelStoreOwner
import kotlin.reflect.KClass

@Composable
@PublishedApi
internal actual fun resolvePlatformBloc(clazz: KClass<*>): Any? {
    val jClass = clazz.java
    if (!ViewModel::class.java.isAssignableFrom(jClass)) return null
    val owner = LocalContext.current.findViewModelStoreOwner() ?: return null
    @Suppress("UNCHECKED_CAST")
    return ViewModelProvider(owner)[jClass as Class<ViewModel>]
}
