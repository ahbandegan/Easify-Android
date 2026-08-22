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
 * Checks if the system is currently in Dark Theme (Night Mode).
 * 
 * This property checks the current UI mode configuration of the device's resources.
 * It is useful for dynamically adjusting UI elements or behavior based on the system's
 * current visual theme preferences.
 * 
 * @return `true` if the device is currently in dark mode (night mode), `false` otherwise.
 */
val Context.isDarkThemeEnabled: Boolean
    get() = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

/**
 * Checks if GPS or Location services are globally enabled on the device.
 * 
 * Determines whether the user has location services turned on in the system settings.
 * On Android P (API 28) and above, it uses `LocationManager.isLocationEnabled()`.
 * On older versions, it falls back to checking `Settings.Secure.LOCATION_MODE`.
 * 
 * Note: This does not verify if your app has the required location permissions, only if 
 * the device-level location setting is enabled.
 * 
 * @return `true` if location services are enabled, `false` otherwise.
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
 * Checks if the device has an active internet connection.
 * 
 * Determines whether there is currently an active network and if it possesses the 
 * `NET_CAPABILITY_INTERNET` capability, indicating it is theoretically capable of 
 * reaching the internet.
 * 
 * Note: This requires the `android.permission.ACCESS_NETWORK_STATE` permission in the manifest.
 * 
 * @return `true` if an active network with internet capability is present, `false` otherwise.
 */
val Context.isInternetAvailable: Boolean
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

/**
 * Checks if the application is currently running on an emulator rather than a physical device.
 * 
 * This method uses a heuristic approach by checking various system properties like 
 * `Build.FINGERPRINT`, `Build.MODEL`, `Build.MANUFACTURER`, `Build.BRAND`, etc.
 * It's useful for disabling certain features (like physical hardware tests or secure storage)
 * when running in an emulated environment.
 * 
 * @return `true` if the device matches known emulator characteristics, `false` if it appears 
 *         to be a physical device.
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
 * Checks if the device is equipped with an NFC (Near Field Communication) hardware chip.
 * 
 * Interrogates the package manager to determine if the system feature `FEATURE_NFC` is present.
 * 
 * @return `true` if the device has NFC hardware, `false` otherwise.
 */
fun Context.hasNfc(): Boolean = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)

/**
 * Checks if the device is equipped with a camera flash hardware.
 * 
 * Interrogates the package manager to determine if the system feature `FEATURE_CAMERA_FLASH` is present.
 * 
 * @return `true` if the device has a camera flash, `false` otherwise.
 */
fun Context.hasCameraFlash(): Boolean = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

/**
 * Checks if the application is currently ignoring battery optimizations.
 * 
 * Determines whether the app has been whitelisted by the user or system to ignore 
 * battery optimization constraints (Doze mode, App Standby).
 * Useful for apps that require persistent background execution.
 * 
 * @return `true` if the application is ignoring battery optimizations, `false` otherwise.
 */
val Context.isIgnoringBatteryOptimizations: Boolean
    get() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager ?: return false
        return powerManager.isIgnoringBatteryOptimizations(packageName)
    }

/**
 * Requests the user to allow the application to ignore battery optimizations.
 * 
 * If the application is already ignoring optimizations (whitelisted), this function does nothing.
 * Otherwise, it attempts to launch the specific intent `ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`
 * to prompt the user directly. If this intent is blocked or unavailable, it falls back to opening 
 * the general battery optimization settings page `ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS`
 * where the user can manually find and whitelist the app.
 * 
 * Note: To use `ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`, you must declare the 
 * `android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` permission in your manifest.
 * Google Play Store policies tightly restrict the use of this permission; ensure your app's 
 * core functionality requires it before requesting it.
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
