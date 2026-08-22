package ir.amirhesambandegan.easify_bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

/**
 * Represents the current connection state of a Bluetooth device.
 */
enum class ConnectionState {
    /** The device is not connected. */
    DISCONNECTED, 
    /** A connection attempt is in progress. */
    CONNECTING, 
    /** The device is successfully connected. */
    CONNECTED, 
    /** The connection is currently being terminated. */
    DISCONNECTING
}

/**
 * Manages GATT (Generic Attribute Profile) connections and operations for a single device.
 * Eliminates the painful boilerplate of BluetoothGattCallback.
 * 
 * @property context The application or activity context.
 */
@SuppressLint("MissingPermission")
class BluetoothConnectionManager(
    private val context: Context
) {
    /**
     * The active [BluetoothGatt] client instance.
     */
    private var gatt: BluetoothGatt? = null
    
    /**
     * Internal state flow tracking the current connection state.
     */
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)

    /**
     * Public observable state flow for the connection state.
     */
    val connectionState: StateFlow<ConnectionState> = _connectionState

    /**
     * Internal state flow maintaining the list of discovered GATT services.
     */
    private val _discoveredServices = MutableStateFlow<List<BluetoothGattService>>(emptyList())

    /**
     * Public observable state flow for the discovered GATT services.
     */
    val discoveredServices: StateFlow<List<BluetoothGattService>> = _discoveredServices

    /**
     * The callback that handles GATT connection state changes and service discovery.
     */
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
