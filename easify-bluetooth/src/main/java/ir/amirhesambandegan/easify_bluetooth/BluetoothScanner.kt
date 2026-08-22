package ir.amirhesambandegan.easify_bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * Handles Bluetooth Low Energy (BLE) device scanning with advanced filtering.
 * 
 * @property context The application or activity context used to access system Bluetooth services.
 */
class BluetoothScanner(context: Context) {

    /**
     * The system [BluetoothManager] retrieved from the context.
     */
    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    
    /**
     * The [android.bluetooth.le.BluetoothLeScanner] used to search for BLE devices.
     */
    private val scanner = bluetoothManager?.adapter?.bluetoothLeScanner

    /**
     * Internal state flow holding the latest list of discovered devices.
     */
    private val _scannedDevices = MutableStateFlow<List<EasifyBluetoothDevice>>(emptyList())
    
    /**
     * A [Flow] providing the list of currently discovered devices.
     */
    val scannedDevices: Flow<List<EasifyBluetoothDevice>> = _scannedDevices

    /**
     * Starts a BLE scan and updates [scannedDevices].
     * Requires proper Bluetooth permissions to be granted.
     * 
     * @param serviceUuids Optional list of UUIDs to filter devices by specific services.
     * @param namePrefix Optional prefix to filter devices by name (e.g. "Easify_").
     */
    @SuppressLint("MissingPermission")
    fun startScan(
        serviceUuids: List<UUID>? = null,
        namePrefix: String? = null
    ) = callbackFlow {
        val callback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val deviceName = result.device.name ?: result.scanRecord?.deviceName
                
                // Manual name filtering if prefix is provided
                if (namePrefix != null && deviceName?.startsWith(namePrefix, ignoreCase = true) != true) {
                    return
                }

                val device = EasifyBluetoothDevice(
                    name = deviceName,
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

        // Build hardware filters if UUIDs are provided
        val filters = serviceUuids?.map { uuid ->
            ScanFilter.Builder().setServiceUuid(ParcelUuid(uuid)).build()
        } ?: emptyList()

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        _scannedDevices.value = emptyList()
        
        if (scanner != null) {
            scanner.startScan(filters, settings, callback)
        } else {
            close()
        }

        awaitClose {
            scanner?.stopScan(callback)
        }
    }
    
    /**
     * Clears the current list of scanned devices, resetting the state to an empty list.
     */
    fun clearDevices() {
        _scannedDevices.value = emptyList()
    }
}
