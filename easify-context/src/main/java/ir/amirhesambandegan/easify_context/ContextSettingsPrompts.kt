package ir.amirhesambandegan.easify_context

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

/**
 * Utility extensions for prompting the user to enable or manage system features.
 * 
 * On Android 10 (API 29) and above, these extensions intelligently utilize the 
 * Settings Panel API to display floating bottom sheets directly within the app, 
 * providing a seamless user experience. On older devices, they gracefully fall back 
 * to opening the full-screen system settings app.
 */

/**
 * Prompts the user to enable or manage Wi-Fi connections.
 * 
 * On Android 10+ (API 29+), this opens a floating bottom sheet containing quick 
 * toggles for Wi-Fi (via `Settings.Panel.ACTION_WIFI`). On older devices, it opens 
 * the traditional full-screen Wi-Fi settings page.
 * 
 * It adds `FLAG_ACTIVITY_NEW_TASK` to the intent, making it safe to call from 
 * any context, including backgrounds or services.
 */
fun Context.promptEnableWifi() {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Intent(Settings.Panel.ACTION_WIFI)
    } else {
        Intent(Settings.ACTION_WIFI_SETTINGS)
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

/**
 * Prompts the user to manage their Internet Connectivity (both Wi-Fi and Cellular Data).
 * 
 * On Android 10+ (API 29+), this opens a floating bottom sheet containing toggles 
 * for both Wi-Fi and mobile data (via `Settings.Panel.ACTION_INTERNET_CONNECTIVITY`).
 * On older devices, it opens the "Wireless & Networks" settings page.
 */
fun Context.promptInternetConnectivity() {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
    } else {
        Intent(Settings.ACTION_WIRELESS_SETTINGS)
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

/**
 * Prompts the user to enable or manage NFC (Near Field Communication).
 * 
 * On Android 10+ (API 29+), this opens a floating bottom sheet to toggle NFC on or off.
 * On older devices, it opens the specific NFC settings page.
 */
fun Context.promptEnableNfc() {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Intent(Settings.Panel.ACTION_NFC)
    } else {
        Intent(Settings.ACTION_NFC_SETTINGS)
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

/**
 * Prompts the user to view and adjust system Volume levels.
 * 
 * On Android 10+ (API 29+), this opens a floating bottom sheet displaying sliders 
 * for Media, Call, Ring, and Alarm volumes (via `Settings.Panel.ACTION_VOLUME`).
 * On older devices, it opens the full Sound settings page.
 */
fun Context.promptVolumeControls() {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Intent(Settings.Panel.ACTION_VOLUME)
    } else {
        Intent(Settings.ACTION_SOUND_SETTINGS)
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

/**
 * Prompts the user to enable Bluetooth.
 * 
 * It primarily attempts to use the `BluetoothAdapter.ACTION_REQUEST_ENABLE` intent, 
 * which usually presents a non-intrusive dialog asking the user to grant permission 
 * to turn on Bluetooth. If this intent is blocked or unsupported, it falls back to 
 * opening the full-screen Bluetooth settings page.
 */
fun Context.promptEnableBluetooth() {
    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        startActivity(intent)
    } catch (e: Exception) {
        // Fallback to bluetooth settings if the request intent is blocked
        val fallback = Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(fallback)
    }
}

/**
 * Prompts the user to enable Location (GPS) services.
 * 
 * Note: Android currently does not provide a Settings Panel bottom sheet for 
 * location services. Therefore, this always opens the full-screen "Location" 
 * settings page (via `Settings.ACTION_LOCATION_SOURCE_SETTINGS`).
 */
fun Context.promptEnableLocation() {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}
