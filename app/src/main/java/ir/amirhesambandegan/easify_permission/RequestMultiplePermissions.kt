package ir.amirhesambandegan.easify_permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

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
