package ir.amirhesambandegan.easify_bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

enum class ConnectionState {
    DISCONNECTED, CONNECTING, CONNECTED, DISCONNECTING
}

/**
 * Manages GATT (Generic Attribute Profile) connections and operations for a single device.
 * Eliminates the painful boilerplate of BluetoothGattCallback.
 */
@SuppressLint("MissingPermission")
class BluetoothConnectionManager(
    private val context: Context
) {
    private var gatt: BluetoothGatt? = null
    
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _discoveredServices = MutableStateFlow<List<BluetoothGattService>>(emptyList())
    val discoveredServices: StateFlow<List<BluetoothGattService>> = _discoveredServices

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    _connectionState.value = ConnectionState.CONNECTED
                    // Automatically start discovering services upon connection
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    _connectionState.value = ConnectionState.DISCONNECTED
                    this@BluetoothConnectionManager.gatt?.close()
                    this@BluetoothConnectionManager.gatt = null
                    _discoveredServices.value = emptyList()
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                _discoveredServices.value = gatt.services
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            // Can be expanded to emit values to a Flow
        }
        
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            // Can be expanded to emit notification values to a Flow
        }
    }

    /**
     * Connects to the given EasifyBluetoothDevice.
     */
    fun connect(device: EasifyBluetoothDevice, autoConnect: Boolean = false) {
        if (_connectionState.value == ConnectionState.CONNECTED) return
        
        _connectionState.value = ConnectionState.CONNECTING
        gatt = device.device.connectGatt(context, autoConnect, gattCallback)
    }

    /**
     * Disconnects the current active connection.
     */
    fun disconnect() {
        _connectionState.value = ConnectionState.DISCONNECTING
        gatt?.disconnect()
    }
    
    /**
     * Helper to easily write data to a characteristic if connected.
     */
    fun writeCharacteristic(serviceUuid: UUID, characteristicUuid: UUID, data: ByteArray): Boolean {
        val gatt = this.gatt ?: return false
        val service = gatt.getService(serviceUuid) ?: return false
        val characteristic = service.getCharacteristic(characteristicUuid) ?: return false
        
        characteristic.value = data
        return gatt.writeCharacteristic(characteristic)
    }
}
