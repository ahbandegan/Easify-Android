package ir.amirhesambandegan.easify_bluetooth

import android.bluetooth.BluetoothDevice

/**
 * A simplified wrapper for [BluetoothDevice] containing essential information.
 *
 * @property name The name of the Bluetooth device. Can be null if not available.
 * @property address The MAC address of the Bluetooth device.
 * @property rssi The Received Signal Strength Indicator (signal strength).
 * @property device The original [BluetoothDevice] instance.
 */
data class EasifyBluetoothDevice(
    val name: String?,
    val address: String,
    val rssi: Int,
    val device: BluetoothDevice
)
