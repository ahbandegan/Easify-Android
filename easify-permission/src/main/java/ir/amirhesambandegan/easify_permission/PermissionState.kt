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
