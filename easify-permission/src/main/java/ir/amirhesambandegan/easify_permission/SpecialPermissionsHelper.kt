package ir.amirhesambandegan.easify_permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

/**
 * Helper utility object to check and request special Android permissions such as system overlay.
 */
object SpecialPermissionsHelper {

    /**
     * Checks whether the application has permission to draw on top of other apps (System Alert Window).
     *
     * @param context The application or activity [Context].
     * @return `true` if overlay permission is granted or if running on API level < 23 (where explicit overlay permission is not required), `false` otherwise.
     */
    fun canDrawOverlays(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    /**
     * Navigates the user to the system settings screen to grant overlay permission (Draw over other apps).
     *
     * Launches the system settings using [Settings.ACTION_MANAGE_OVERLAY_PERMISSION]
     * if the permission has not yet been granted on Android M (API 23) and above.
     *
     * @param context The application or activity [Context] used to start the intent.
     */
    fun requestOverlayPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.packageName}")
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }
        }
    }
}
