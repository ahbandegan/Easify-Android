package ir.amirhesambandegan.easify_permission

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

/**
 * A headless Composable effect that requests a single runtime permission automatically on first composition.
 *
 * If the permission is already granted, [onGrant] is invoked immediately.
 * Otherwise, the permission request launcher is displayed and the result invokes [onGrant] or [onDenied].
 *
 * @param permission The runtime permission string to check and request (e.g. `Manifest.permission.CAMERA`).
 * @param onGrant Callback invoked when the permission is granted.
 * @param onDenied Callback invoked when the permission is denied.
 */
@Composable
fun RequestPermission(
    permission: String,
    onGrant: () -> Unit,
    onDenied: () -> Unit
) {
    val context = LocalContext.current
    val result = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { res ->
        if (res)
            onGrant()
        else
            onDenied()
    }

    LaunchedEffect(Unit) {
        if (isGranted(permission, context) == PermissionStats.PERMISSION_GRANTED)
            onGrant()
        else
            result.launch(permission)
    }
}
