package ir.amirhesambandegan.easify_bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothStatusCodes
import android.content.Context
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

/**
 * Represents the current connection state of a Bluetooth device.
 */
enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    DISCONNECTING
}

/**
 * Manages BLE GATT connections and characteristic writes cleanly across Android API levels
 * (including Android 13+ non-deprecated `writeCharacteristic` APIs).
 */
@SuppressLint("MissingPermission")
class BluetoothConnectionManager(context: Context) {
    private val appContext = context.applicationContext
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
    }

    /**
     * Connects to [device]. No-op if already connected or connecting.
     */
    fun connect(device: EasifyBluetoothDevice, autoConnect: Boolean = false) {
        if (_connectionState.value == ConnectionState.CONNECTED ||
            _connectionState.value == ConnectionState.CONNECTING
        ) return

        _connectionState.value = ConnectionState.CONNECTING
        gatt = device.device.connectGatt(appContext, autoConnect, gattCallback)
    }

    /**
     * Disconnects and closes any active GATT connection.
     */
    fun disconnect() {
        val current = gatt
        if (current == null) {
            _connectionState.value = ConnectionState.DISCONNECTED
            return
        }
        _connectionState.value = ConnectionState.DISCONNECTING
        current.disconnect()
    }

    /**
     * Writes [data] to [characteristicUuid] inside [serviceUuid] using Android 13+ APIs where supported.
     */
    fun writeCharacteristic(
        serviceUuid: UUID,
        characteristicUuid: UUID,
        data: ByteArray,
        writeType: Int = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
    ): Boolean {
        val activeGatt = gatt ?: return false
        val service = activeGatt.getService(serviceUuid) ?: return false
        val characteristic = service.getCharacteristic(characteristicUuid) ?: return false

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activeGatt.writeCharacteristic(characteristic, data, writeType) ==
                BluetoothStatusCodes.SUCCESS
        } else {
            @Suppress("DEPRECATION")
            run {
                characteristic.writeType = writeType
                characteristic.value = data
                activeGatt.writeCharacteristic(characteristic)
            }
        }
    }
}
