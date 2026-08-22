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
 * Checks if a specific application is currently installed on the device.
 * 
 * This method securely checks the package manager for the target [packageName].
 * Note: For applications targeting Android 11 (API level 30) or higher, you must declare a
 * `<queries>` element in your `AndroidManifest.xml` specifying the packages you intend to query,
 * otherwise this method may return false even if the app is installed.
 *
 * @param packageName The application ID / package name to verify (e.g., "com.whatsapp").
 * @return `true` if the application is installed on the device, `false` otherwise or if it cannot be found.
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
 * Retrieves the current application's version name.
 * 
 * This property extracts the `versionName` defined in the app's `build.gradle` or `AndroidManifest.xml`.
 * It is commonly used for displaying the version to the user or for logging purposes.
 * 
 * @return The version name string of the application (e.g., "1.0.0"). Returns "Unknown" if the 
 *         package information cannot be found.
 */
val Context.appVersionName: String
    get() = try {
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        pInfo.versionName ?: "Unknown"
    } catch (e: PackageManager.NameNotFoundException) {
        "Unknown"
    }

/**
 * Retrieves the current application's version code.
 * 
 * This property extracts the `versionCode` defined in the app's `build.gradle` or `AndroidManifest.xml`.
 * It is typically used for internal version tracking, migrations, or update checks.
 * Handles the deprecation of `versionCode` in newer Android versions by using `longVersionCode`
 * on Android P (API 28) and above.
 * 
 * @return The version code of the application as a [Long]. Returns `0L` if the package information 
 *         cannot be found.
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
 * Opens the Google Play Store page for a specified application package.
 * 
 * If the [targetPackageName] is not provided, it defaults to the current application's package name.
 * This function first attempts to open the Play Store app using the `market://` URI scheme.
 * If the Play Store app is not installed or cannot handle the intent, it gracefully falls back 
 * to opening the app's page in a web browser using the standard Play Store website URL.
 *
 * @param targetPackageName The package name of the application to view in the Play Store. 
 *                          Defaults to [Context.getPackageName].
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
