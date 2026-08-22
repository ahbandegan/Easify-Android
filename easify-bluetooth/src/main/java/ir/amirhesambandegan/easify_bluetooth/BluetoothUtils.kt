package ir.amirhesambandegan.easify_bluetooth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import ir.amirhesambandegan.easify_context.findActivity

/**
 * Utility functions for Bluetooth permissions and settings.
 */
object BluetoothUtils {

    /**
     * Returns the required permissions for Bluetooth operations based on the Android version.
     * Handled differences between Android 12+ (API 31) and older versions.
     * 
     * @return A list of permission strings required for Bluetooth scanning, connecting, and advertising.
     */
    fun getRequiredPermissions(): List<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION.SDK_INT) { // simplified for now, will refine
            if (Build.VERSION.SDK_INT >= 31) {
                listOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_ADVERTISE
                )
            } else {
                listOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        } else {
            listOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)
        }
    }

    /**
     * Opens the system Bluetooth settings screen.
     * This is an extension function on [Context] that launches the settings activity in a new task.
     */
    fun Context.openBluetoothSettings() {
        val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }
}
