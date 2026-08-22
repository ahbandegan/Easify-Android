package ir.amirhesambandegan.easify_permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * Opens the specific Android App Settings page for this application.
 * Useful for when a permission is permanently denied and the user must enable it manually.
 */
fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}
