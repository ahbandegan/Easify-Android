package ir.amirhesambandegan.easify_context

import android.content.Context
import android.content.res.Configuration
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

/**
 * Utility extensions for checking device state, hardware, and environment.
 */

/**
 * Checks if the system is currently in Dark Theme.
 */
val Context.isDarkThemeEnabled: Boolean
    get() = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

/**
 * Checks if GPS/Location services are enabled on the device.
 */
val Context.isLocationEnabled: Boolean
    get() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManager?.isLocationEnabled == true
        } else {
            val mode = Settings.Secure.getInt(
                contentResolver,
                Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }

/**
 * Checks if there is an active internet connection.
 */
val Context.isInternetAvailable: Boolean
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

/**
 * Checks if the app is running on an emulator.
 */
fun Context.isEmulator(): Boolean {
    return (Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
            || "google_sdk" == Build.PRODUCT)
}

/**
 * Checks if the device has an NFC hardware chip.
 */
fun Context.hasNfc(): Boolean = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)

/**
 * Checks if the device has a camera flash.
 */
fun Context.hasCameraFlash(): Boolean = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

/**
 * Checks if the app is ignoring battery optimizations (whitelisted).
 */
val Context.isIgnoringBatteryOptimizations: Boolean
    get() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager ?: return false
        return powerManager.isIgnoringBatteryOptimizations(packageName)
    }

/**
 * Opens the battery optimization settings so the user can whitelist the app.
 */
fun Context.requestIgnoreBatteryOptimizations() {
    if (isIgnoringBatteryOptimizations) return
    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
        data = Uri.parse("package:$packageName")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        startActivity(intent)
    } catch (e: Exception) {
        // Fallback to normal battery settings if the specific intent is blocked
        val fallbackIntent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(fallbackIntent)
    }
}
