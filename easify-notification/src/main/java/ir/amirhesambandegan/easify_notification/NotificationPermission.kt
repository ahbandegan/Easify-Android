package ir.amirhesambandegan.easify_notification

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

/**
 * Remembers and provides the state and control callbacks for handling Android notification permissions in Jetpack Compose.
 * Handles Android 13+ (API 33, Tiramisu) POST_NOTIFICATIONS permission requests automatically.
 *
 * @return A [NotificationPermissionState] instance holding the current permission status and action handlers.
 */
@Composable
fun rememberNotificationPermission(): NotificationPermissionState {
    val context = LocalContext.current
    var isGranted by remember { mutableStateOf(checkPermission(context)) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        isGranted = granted
    }

    return remember {
        NotificationPermissionState(
            isGranted = isGranted,
            requestPermission = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    isGranted = true
                }
            },
            openSettings = {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                }
                context.startActivity(intent)
            }
        )
    }
}

/**
 * Checks whether the POST_NOTIFICATIONS permission is granted on the current device.
 *
 * @param context The [Context] used to check the permission status.
 * @return `true` if permission is granted or not required (pre-Android 13), `false` otherwise.
 */
private fun checkPermission(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }
    return true
}

/**
 * Represents the state and actions for managing notification permissions.
 *
 * @property isGranted Indicates whether notification permission is currently granted.
 * @property requestPermission Action to request the notification permission from the user.
 * @property openSettings Action to open the application's notification settings screen.
 */
class NotificationPermissionState(
    val isGranted: Boolean,
    val requestPermission: () -> Unit,
    val openSettings: () -> Unit
)
