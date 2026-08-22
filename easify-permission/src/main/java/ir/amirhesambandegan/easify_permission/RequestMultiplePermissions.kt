package ir.amirhesambandegan.easify_permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

/**
 * A headless Composable effect that requests a list of runtime permissions automatically on first composition.
 *
 * If all permissions are already granted, [onGrant] is invoked immediately.
 * Otherwise, the permission request launcher is displayed and the result invokes [onGrant] or [onDenied].
 *
 * @param permissions The list of runtime permission strings to check and request.
 * @param onGrant Callback invoked when all requested permissions are granted.
 * @param onDenied Callback invoked when one or more permissions are denied.
 */
@Composable
fun RequestMultiplePermissions(
    permissions: List<String>,
    onGrant: () -> Unit,
    onDenied: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        if (resultMap.values.all { it })
            onGrant()
        else
            onDenied()
    }

    LaunchedEffect(Unit) {
        val allGranted = permissions.all {
            isGranted(it, context) == PermissionStats.PERMISSION_GRANTED
        }
        if (allGranted)
            onGrant()
        else
            launcher.launch(permissions.toTypedArray())
    }
}
