package ir.amirhesambandegan.easify_bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update

/**
 * Handles Bluetooth Low Energy (BLE) device scanning.
 */
class BluetoothScanner(context: Context) {

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val scanner = bluetoothManager?.adapter?.bluetoothLeScanner

    private val _scannedDevices = MutableStateFlow<List<EasifyBluetoothDevice>>(emptyList())
    
    /**
     * A [Flow] providing the list of currently discovered devices.
     */
    val scannedDevices: Flow<List<EasifyBluetoothDevice>> = _scannedDevices

    /**
     * Starts a BLE scan and updates [scannedDevices].
     * Requires proper Bluetooth permissions to be granted.
     */
    @SuppressLint("MissingPermission")
    fun startScan() = callbackFlow {
        val callback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device = EasifyBluetoothDevice(
                    name = result.device.name,
                    address = result.device.address,
                    rssi = result.rssi,
                    device = result.device
                )
                
                _scannedDevices.update { current ->
                    if (current.any { it.address == device.address }) {
                        current.map { if (it.address == device.address) device else it }
                    } else {
                        current + device
                    }
                }
                trySend(Unit)
            }
        }

        _scannedDevices.value = emptyList()
        scanner?.startScan(callback)

        awaitClose {
            scanner?.stopScan(callback)
        }
    }
}
