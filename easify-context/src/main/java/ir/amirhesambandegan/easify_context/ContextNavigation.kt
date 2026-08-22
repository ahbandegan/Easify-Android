package ir.amirhesambandegan.easify_context

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings

/**
 * Utility extensions for starting Activities and opening system settings screens.
 */

/**
 * Starts an activity of type [T] concisely and allows for intent configuration via a lambda.
 * 
 * This inline function simplifies the standard boilerplate of creating an [Intent], optionally 
 * putting extras or flags into it, and starting the activity. If the function is called from a 
 * non-Activity [Context] (like an Application or Service), the `FLAG_ACTIVITY_NEW_TASK` flag 
 * is automatically added to prevent runtime crashes.
 *
 * Example usage:
 * ```kotlin
 * context.start<MainActivity>()
 * context.start<ProfileActivity> { putExtra("id", 123) }
 * ```
 *
 * @param T The type of the [Activity] to be started.
 * @param options Additional options for how the Activity should be started (e.g., transition animations).
 * @param block An optional lambda block to configure the [Intent] before starting the activity.
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
 * Restarts the application by launching its default launcher activity and exiting the current process.
 * 
 * This method finds the intent associated with the main launcher activity for the current package.
 * It then restarts the task, effectively clearing the previous activity backstack, and calls 
 * `Runtime.getRuntime().exit(0)` to force the process to restart. This can be useful for 
 * applying language changes or resetting the app state completely.
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
 * Opens the system settings screen for Wi-Fi.
 * 
 * This starts the [Settings.ACTION_WIFI_SETTINGS] intent, allowing the user to view or modify 
 * their Wi-Fi connections. It adds the `FLAG_ACTIVITY_NEW_TASK` flag so it can be called 
 * from any context.
 */
fun Context.openWifiSettings() {
    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}

/**
 * Opens the system settings screen for Location services.
 * 
 * This starts the [Settings.ACTION_LOCATION_SOURCE_SETTINGS] intent, allowing the user to toggle 
 * device-level location settings. It adds the `FLAG_ACTIVITY_NEW_TASK` flag so it can be called 
 * from any context.
 */
fun Context.openLocationSettings() {
    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}

/**
 * Opens the system settings screen for Bluetooth.
 * 
 * This starts the [Settings.ACTION_BLUETOOTH_SETTINGS] intent, allowing the user to view or modify 
 * their Bluetooth connections and paired devices. It adds the `FLAG_ACTIVITY_NEW_TASK` flag 
 * so it can be called from any context.
 */
fun Context.openBluetoothSettings() {
    startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}

/**
 * Opens the system settings screen for Display options.
 * 
 * This starts the [Settings.ACTION_DISPLAY_SETTINGS] intent, allowing the user to modify display 
 * properties like brightness, timeout, and font size. It adds the `FLAG_ACTIVITY_NEW_TASK` flag 
 * so it can be called from any context.
 */
fun Context.openDisplaySettings() {
    startActivity(Intent(Settings.ACTION_DISPLAY_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}
