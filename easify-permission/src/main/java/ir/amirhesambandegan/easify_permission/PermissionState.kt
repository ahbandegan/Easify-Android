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

enum class PermissionStatus {
    GRANTED,
    DENIED_SHOULD_SHOW_RATIONALE,
    PERMANENTLY_DENIED
}

class PermissionState(
    val permission: String,
    val status: PermissionStatus,
    val launchPermissionRequest: () -> Unit
)

@Composable
fun rememberPermissionState(permission: String): PermissionState {
    val context = LocalContext.current
    val activity = remember(context) { context.findHostActivity() }
    val lifecycleOwner = LocalLifecycleOwner.current

    var status by remember(permission, context) {
        mutableStateOf(getPermissionStatus(context, activity, permission))
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
        status = getPermissionStatus(context, activity, permission)
    }

    DisposableEffect(lifecycleOwner, permission, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                status = getPermissionStatus(context, activity, permission)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return remember(permission, status, launcher) {
        PermissionState(
            permission = permission,
            status = status,
            launchPermissionRequest = { launcher.launch(permission) }
        )
    }
}

private fun Context.findHostActivity(): Activity? {
    var current: Context? = this
    val visited = Collections.newSetFromMap(IdentityHashMap<Context, Boolean>())
    while (current is ContextWrapper && visited.add(current)) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}

private fun getPermissionStatus(context: Context, activity: Activity?, permission: String): PermissionStatus {
    val isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    if (isGranted) return PermissionStatus.GRANTED

    val shouldShowRationale = activity?.let {
        ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
    } ?: false

    return if (shouldShowRationale) {
        PermissionStatus.DENIED_SHOULD_SHOW_RATIONALE
    } else {
        PermissionStatus.PERMANENTLY_DENIED
    }
}
