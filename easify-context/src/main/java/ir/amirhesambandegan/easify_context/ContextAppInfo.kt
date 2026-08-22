package ir.amirhesambandegan.easify_context

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build

/**
 * Utility extensions for Application package information.
 */

/**
 * Checks if an application is installed on the device.
 * Note: Android 11+ requires <queries> block in AndroidManifest.xml for specific packages.
 *
 * @param packageName The package name to check (e.g., "com.whatsapp").
 * @return True if installed, false otherwise.
 */
fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0)
        }
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

/**
 * Gets the current app's version name (e.g., "1.0.0").
 */
val Context.appVersionName: String
    get() = try {
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        pInfo.versionName ?: "Unknown"
    } catch (e: PackageManager.NameNotFoundException) {
        "Unknown"
    }

/**
 * Gets the current app's version code.
 */
val Context.appVersionCode: Long
    get() = try {
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            pInfo.versionCode.toLong()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        0L
    }

/**
 * Opens the Google Play Store page for a specific application.
 * Defaults to the current application's package name.
 */
fun Context.openPlayStore(targetPackageName: String = packageName) {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$targetPackageName")
            ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        )
    } catch (e: android.content.ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$targetPackageName")
            ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        )
    }
}
