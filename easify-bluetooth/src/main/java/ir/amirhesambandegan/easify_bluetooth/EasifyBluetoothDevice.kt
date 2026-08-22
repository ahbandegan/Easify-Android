package ir.amirhesambandegan.easify_bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import kotlin.math.pow

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
) {
    /**
     * Estimates the distance to the Bluetooth device in meters based on RSSI.
     * Note: This is an approximation. Environmental factors heavily affect RSSI.
     * 
     * @param txPower The known transmission power at 1 meter. Default is usually -59 for BLE.
     * @return Estimated distance in meters, or -1.0 if RSSI is 0.
     */
    fun getEstimatedDistance(txPower: Int = -59): Double {
        if (rssi == 0) return -1.0
        val ratio = rssi * 1.0 / txPower
        if (ratio < 1.0) {
            return ratio.pow(10.0)
        } else {
            return (0.89976 * ratio.pow(7.7095)) + 0.111
        }
    }

    /**
     * Checks if the device is already bonded (paired) with the system.
     * 
     * @return `true` if the device is bonded, `false` otherwise.
     */
    @SuppressLint("MissingPermission")
    fun isBonded(): Boolean {
        return device.bondState == BluetoothDevice.BOND_BONDED
    }

    /**
     * Initiates bonding (pairing) with the device.
     * 
     * @return `true` if the bonding process was successfully initiated, `false` otherwise.
     */
    @SuppressLint("MissingPermission")
    fun pair(): Boolean {
        return try {
            device.createBond()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Attempts to remove the bond (unpair) using reflection, as `removeBond` is a hidden API.
     * 
     * @return `true` if the unpairing was successfully initiated, `false` otherwise.
     */
    fun unpair(): Boolean {
        return try {
            val method = device.javaClass.getMethod("removeBond")
            method.invoke(device) as Boolean
        } catch (e: Exception) {
            false
        }
    }
}
