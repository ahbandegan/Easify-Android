package ir.amirhesambandegan.easify_permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

/**
 * Headless Composable effect that requests [permission] automatically whenever [permission] changes
 * or on initial composition.
 */
@Composable
fun RequestPermission(
    permission: String,
    onGrant: () -> Unit,
    onDenied: () -> Unit
) {
    val context = LocalContext.current
    val latestOnGrant by rememberUpdatedState(onGrant)
    val latestOnDenied by rememberUpdatedState(onDenied)

    val result = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) latestOnGrant() else latestOnDenied()
    }

    LaunchedEffect(permission, context) {
        if (isGranted(permission, context) == PermissionStats.PERMISSION_GRANTED) {
            latestOnGrant()
        } else {
            result.launch(permission)
        }
    }
}
