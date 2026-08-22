package ir.amirhesambandegan.easify_context

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings

/**
 * Utility extensions for starting Activities and opening system settings.
 */

/**
 * Starts an activity of type [T] concisely.
 *
 * Example:
 * `context.start<MainActivity>()`
 * `context.start<ProfileActivity> { putExtra("id", 123) }`
 */
inline fun <reified T : Activity> Context.start(
    options: Bundle? = null,
    crossinline block: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.block()
    // If we're starting from an Application context or Service, we need NEW_TASK
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent, options)
}

/**
 * Restarts the application by launching the launcher activity and killing the current process.
 */
fun Context.restartApp() {
    val packageManager = packageManager
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    val componentName = intent?.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}

/**
 * Opens the device Wi-Fi settings.
 */
fun Context.openWifiSettings() {
    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}

/**
 * Opens the device Location settings.
 */
fun Context.openLocationSettings() {
    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}

/**
 * Opens the device Bluetooth settings.
 */
fun Context.openBluetoothSettings() {
    startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}

/**
 * Opens the device Display settings.
 */
fun Context.openDisplaySettings() {
    startActivity(Intent(Settings.ACTION_DISPLAY_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}
