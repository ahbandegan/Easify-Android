package ir.amirhesambandegan.easify_permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * Opens the application details settings screen for this application in the Android system settings.
 *
 * This extension function creates an intent with [Settings.ACTION_APPLICATION_DETAILS_SETTINGS] targeting
 * the current package name, allowing users to manually grant permissions or manage app settings when permissions
 * have been permanently denied or when direct configuration is required.
 */
fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}
