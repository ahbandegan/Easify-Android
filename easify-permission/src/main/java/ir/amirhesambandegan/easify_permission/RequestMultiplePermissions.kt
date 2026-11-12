package ir.amirhesambandegan.easify_permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

/**
 * Headless Composable effect that requests all [permissions] automatically.
 * Immediately invokes [onGrant] if [permissions] is empty or already granted.
 */
@Composable
fun RequestMultiplePermissions(
    permissions: List<String>,
    onGrant: () -> Unit,
    onDenied: () -> Unit
) {
    val context = LocalContext.current
    val latestOnGrant by rememberUpdatedState(onGrant)
    val latestOnDenied by rememberUpdatedState(onDenied)

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        if (resultMap.values.all { it }) latestOnGrant() else latestOnDenied()
    }

    LaunchedEffect(permissions, context) {
        if (permissions.isEmpty()) {
            latestOnGrant()
            return@LaunchedEffect
        }
        val allGranted = permissions.all {
            isGranted(it, context) == PermissionStats.PERMISSION_GRANTED
        }
        if (allGranted) {
            latestOnGrant()
        } else {
            launcher.launch(permissions.toTypedArray())
        }
    }
}
