package ir.amirhesambandegan.easify_permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Represents the granular status of an Android runtime permission.
 */
enum class PermissionStatus {
    /** The permission has been granted by the user. */
    GRANTED,
    
    /** The permission was denied and the app should present an educational rationale to explain why the permission is needed. */
    DENIED_SHOULD_SHOW_RATIONALE,
    
    /** The permission was permanently denied ("Don't ask again" selected) or has not been requested yet. */
    PERMANENTLY_DENIED
}

/**
 * Holds the state of a single permission request in Jetpack Compose.
 *
 * @property permission The permission identifier string being requested (e.g. `Manifest.permission.CAMERA`).
 * @property status The current [PermissionStatus] representing granted, rationale needed, or permanently denied.
 * @property launchPermissionRequest Callback to trigger the permission request dialog.
 */
class PermissionState(
    val permission: String,
    val status: PermissionStatus,
    val launchPermissionRequest: () -> Unit
)

/**
 * Remembers and observes the permission state for a single runtime permission in Jetpack Compose.
 *
 * @param permission The runtime permission string to observe and request.
 * @return A [PermissionState] instance containing the current status and a launch function.
 */
@Composable
fun rememberPermissionState(permission: String): PermissionState {
    val context = LocalContext.current
    val activity = context as? Activity
    
    var status by remember { mutableStateOf(getPermissionStatus(context, activity, permission)) }
    
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
        status = getPermissionStatus(context, activity, permission)
    }
    
    return remember(status) {
        PermissionState(
            permission = permission,
            status = status,
            launchPermissionRequest = { launcher.launch(permission) }
        )
    }
}

/**
 * Determines the current [PermissionStatus] of a given permission.
 *
 * @param context The [Context] to check if permission is granted.
 * @param activity The optional host [Activity] to check whether rationale should be displayed.
 * @param permission The permission identifier to check.
 * @return The evaluated [PermissionStatus].
 */
private fun getPermissionStatus(context: Context, activity: Activity?, permission: String): PermissionStatus {
    val isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    if (isGranted) return PermissionStatus.GRANTED
    
    val shouldShowRationale = activity?.let {
        ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
    } ?: false
    
    return if (shouldShowRationale) {
        PermissionStatus.DENIED_SHOULD_SHOW_RATIONALE
    } else {
        // Technically, this could also be the first time asking, but in practice 
        // it serves as the blocked state if already asked.
        PermissionStatus.PERMANENTLY_DENIED 
    }
}
