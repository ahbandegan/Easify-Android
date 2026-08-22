package ir.amirhesambandegan.easify_bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.os.ParcelUuid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

/**
 * Handles converting the Android device into a BLE peripheral (Beacon/Advertiser).
 */
@SuppressLint("MissingPermission")
class BluetoothAdvertiser(context: Context) {
    
    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val advertiser = bluetoothManager?.adapter?.bluetoothLeAdvertiser
    
    private val _isAdvertising = MutableStateFlow(false)
    val isAdvertising: StateFlow<Boolean> = _isAdvertising
    
    private var advertiseCallback: AdvertiseCallback? = null

    /**
     * Starts broadcasting BLE signals.
     * 
     * @param serviceUuid The UUID other devices will use to find you.
     * @param includeDeviceName Whether to broadcast the phone's bluetooth name.
     * @param onSuccess Callback triggered when advertising starts successfully.
     * @param onError Callback triggered if advertising fails (e.g. not supported).
     */
    fun startAdvertising(
        serviceUuid: UUID,
        includeDeviceName: Boolean = true,
        onSuccess: () -> Unit = {},
        onError: (errorCode: Int) -> Unit = {}
    ) {
        if (_isAdvertising.value || advertiser == null) return

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(true)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(includeDeviceName)
            .addServiceUuid(ParcelUuid(serviceUuid))
            .build()

        advertiseCallback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                _isAdvertising.value = true
                onSuccess()
            }

            override fun onStartFailure(errorCode: Int) {
                _isAdvertising.value = false
                onError(errorCode)
            }
        }

        advertiser.startAdvertising(settings, data, advertiseCallback)
    }

    /**
     * Stops broadcasting.
     */
    fun stopAdvertising() {
        if (!_isAdvertising.value) return
        advertiseCallback?.let {
            advertiser?.stopAdvertising(it)
        }
        advertiseCallback = null
        _isAdvertising.value = false
    }
}
