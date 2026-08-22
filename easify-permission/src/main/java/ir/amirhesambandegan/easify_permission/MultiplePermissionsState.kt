package ir.amirhesambandegan.easify_permission

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

/**
 * Holds the state of multiple permission requests in Jetpack Compose.
 *
 * @property permissions The list of permissions being requested.
 * @property allPermissionsGranted `true` if all permissions in [permissions] are granted, `false` otherwise.
 * @property shouldShowRationale `true` if the UI should display an educational rationale before requesting permissions.
 * @property launchMultiplePermissionRequest Callback to trigger the permission request dialog for all permissions.
 */
class MultiplePermissionsState(
    val permissions: List<String>,
    val allPermissionsGranted: Boolean,
    val shouldShowRationale: Boolean,
    val launchMultiplePermissionRequest: () -> Unit
)

/**
 * Remembers and observes the permission state for multiple runtime permissions in Jetpack Compose.
 *
 * @param permissions The list of runtime permission strings to observe and request (e.g. `Manifest.permission.CAMERA`).
 * @return A [MultiplePermissionsState] instance containing the current status and a launch function.
 */
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

/**
 * Checks the grant and rationale status for a collection of permissions.
 *
 * @param context The [android.content.Context] to check permission status.
 * @param activity The optional host [Activity] to check if a rationale should be shown.
 * @param permissions The list of permission identifiers to check.
 * @return A [Pair] where the first element indicates whether all permissions are granted, and the second element indicates whether rationale should be shown for any of them.
 */
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
