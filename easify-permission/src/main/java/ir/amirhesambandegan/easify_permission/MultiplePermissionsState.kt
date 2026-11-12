package ir.amirhesambandegan.easify_permission

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.Collections
import java.util.IdentityHashMap

class MultiplePermissionsState(
    val permissions: List<String>,
    val allPermissionsGranted: Boolean,
    val shouldShowRationale: Boolean,
    val launchMultiplePermissionRequest: () -> Unit
)

@Composable
fun rememberMultiplePermissionsState(permissions: List<String>): MultiplePermissionsState {
    val context = LocalContext.current
    val activity = remember(context) { context.findMultipleHostActivity() }
    val lifecycleOwner = LocalLifecycleOwner.current

    var state by remember(permissions, context) {
        mutableStateOf(getMultiplePermissionsState(context, activity, permissions))
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ ->
        state = getMultiplePermissionsState(context, activity, permissions)
    }

    DisposableEffect(lifecycleOwner, permissions, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                state = getMultiplePermissionsState(context, activity, permissions)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return remember(permissions, state, launcher) {
        MultiplePermissionsState(
            permissions = permissions,
            allPermissionsGranted = state.first,
            shouldShowRationale = state.second,
            launchMultiplePermissionRequest = { launcher.launch(permissions.toTypedArray()) }
        )
    }
}

private fun Context.findMultipleHostActivity(): Activity? {
    var current: Context? = this
    val visited = Collections.newSetFromMap(IdentityHashMap<Context, Boolean>())
    while (current is ContextWrapper && visited.add(current)) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}

private fun getMultiplePermissionsState(
    context: Context,
    activity: Activity?,
    permissions: List<String>
): Pair<Boolean, Boolean> {
    if (permissions.isEmpty()) return true to false
    val allGranted = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
    val shouldShowRationale = activity?.let { act ->
        permissions.any { ActivityCompat.shouldShowRequestPermissionRationale(act, it) }
    } ?: false

    return Pair(allGranted, shouldShowRationale)
}
