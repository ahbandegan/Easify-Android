package ir.amirhesambandegan.easify_context

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings

/**
 * Starts an activity of type [T] concisely, adding `FLAG_ACTIVITY_NEW_TASK` when called from a non-Activity context.
 */
inline fun <reified T : Activity> Context.start(
    options: Bundle? = null,
    crossinline block: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.block()
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent, options)
}

/**
 * Safely restarts the application by launching its default launcher task.
 */
fun Context.restartApp() {
    val launchIntent = packageManager.getLaunchIntentForPackage(packageName) ?: return
    val componentName = launchIntent.component ?: return
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}

/**
 * Opens system Wi-Fi settings safely.
 */
fun Context.openWifiSettings() {
    launchSettingsAction(Settings.ACTION_WIFI_SETTINGS)
}

/**
 * Opens system Location settings safely.
 */
fun Context.openLocationSettings() {
    launchSettingsAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
}

/**
 * Opens system Bluetooth settings safely.
 */
fun Context.openBluetoothSettings() {
    launchSettingsAction(Settings.ACTION_BLUETOOTH_SETTINGS)
}

/**
 * Opens system Display settings safely.
 */
fun Context.openDisplaySettings() {
    launchSettingsAction(Settings.ACTION_DISPLAY_SETTINGS)
}

private fun Context.launchSettingsAction(action: String) {
    val intent = Intent(action).apply {
        if (this@launchSettingsAction !is Activity) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        runCatching {
            startActivity(
                Intent(Settings.ACTION_SETTINGS).apply {
                    if (this@launchSettingsAction !is Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
    }
}
