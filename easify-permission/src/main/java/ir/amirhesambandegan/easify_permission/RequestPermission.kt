package ir.amirhesambandegan.easify_permission

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

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
