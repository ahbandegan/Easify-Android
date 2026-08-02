package ir.amirhesambandegan.easify_file

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import ir.amirhesambandegan.easify_permission.PermissionUtils
import ir.amirhesambandegan.easify_permission.RequestMultiplePermissions

/**
 * A Composable that launches a file picker to select a single visual media file.
 * It handles the necessary permissions based on the [mediaType] and the device's API level.
 *
 * @param mediaType The type of media to pick (e.g., ImageOnly, VideoOnly, or ImageAndVideo).
 * @param onResult Callback invoked with the [Uri] of the picked file, or null if none was selected.
 */
@Composable
fun FilePicker(mediaType: MediaType, onResult: (Uri?) -> Unit) {
    val context = LocalContext.current
    var showPermissionRequester by remember { mutableStateOf(false) }
    var permissionsGranted by remember { mutableStateOf(false) }

    val pickerLauncher = rememberLauncherForActivityResult(
        PickVisualMedia(),
        onResult,
    )

    val targetType = when (mediaType) {
        MediaType.ImageOnly -> PickVisualMedia.ImageOnly
        MediaType.VideoOnly -> PickVisualMedia.VideoOnly
        MediaType.ImageAndVideo -> PickVisualMedia.ImageAndVideo
    }

    val requiredPermissions = remember(mediaType) {
        PermissionUtils.getRequiredPermissions(context, mediaType)
    }

    LaunchedEffect(Unit) {
        if (requiredPermissions.isEmpty()) {
            permissionsGranted = true
        } else {
            showPermissionRequester = true
        }
    }

    if (showPermissionRequester) {
        RequestMultiplePermissions(
            permissions = requiredPermissions,
            onGrant = {
                showPermissionRequester = false
                permissionsGranted = true
            },
            onDenied = {
                showPermissionRequester = false
                onResult(null)
            }
        )
    }

    LaunchedEffect(permissionsGranted) {
        if (permissionsGranted) {
            pickerLauncher.launch(PickVisualMediaRequest(targetType))
        }
    }
}
