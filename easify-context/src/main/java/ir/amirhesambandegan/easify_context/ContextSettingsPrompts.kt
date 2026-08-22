package ir.amirhesambandegan.easify_context

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

/**
 * Utility extensions for prompting the user to enable system features.
 * Utilizes the beautiful floating Settings Panels on Android 10+ when available.
 */

/**
 * Prompts the user to enable Wi-Fi.
 * On Android 10+ (API 29+), this opens a floating bottom sheet (Settings Panel).
 * On older devices, it opens the Wi-Fi settings page.
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
 * Prompts the user to manage Internet Connectivity (Wi-Fi and Cellular Data).
 * On Android 10+ (API 29+), this opens a floating bottom sheet.
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
 * Prompts the user to enable NFC.
 * On Android 10+ (API 29+), this opens a floating bottom sheet.
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
 * Prompts the user to adjust Volume levels.
 * On Android 10+ (API 29+), this opens a floating bottom sheet with volume sliders.
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
 * Prompts the user to enable Bluetooth via a seamless system dialog.
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
 * Prompts the user to enable Location (GPS).
 * Note: Android does not provide a Settings Panel for location, so this opens the settings page.
 */
fun Context.promptEnableLocation() {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}
