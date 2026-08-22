package ir.amirhesambandegan.easify_permission

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

class MultiplePermissionsState(
    val permissions: List<String>,
    val allPermissionsGranted: Boolean,
    val shouldShowRationale: Boolean,
    val launchMultiplePermissionRequest: () -> Unit
)

@Composable
fun rememberMultiplePermissionsState(permissions: List<String>): MultiplePermissionsState {
    val context = LocalContext.current
    val activity = context as? Activity
    
    var state by remember { mutableStateOf(getMultiplePermissionsState(context, activity, permissions)) }
    
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ ->
        state = getMultiplePermissionsState(context, activity, permissions)
    }
    
    return remember(state) {
        MultiplePermissionsState(
            permissions = permissions,
            allPermissionsGranted = state.first,
            shouldShowRationale = state.second,
            launchMultiplePermissionRequest = { launcher.launch(permissions.toTypedArray()) }
        )
    }
}

private fun getMultiplePermissionsState(
    context: android.content.Context, 
    activity: Activity?, 
    permissions: List<String>
): Pair<Boolean, Boolean> {
    val allGranted = permissions.all { 
        androidx.core.content.ContextCompat.checkSelfPermission(context, it) == android.content.pm.PackageManager.PERMISSION_GRANTED 
    }
    val shouldShowRationale = activity?.let { act ->
        permissions.any { androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(act, it) }
    } ?: false
    
    return Pair(allGranted, shouldShowRationale)
}
