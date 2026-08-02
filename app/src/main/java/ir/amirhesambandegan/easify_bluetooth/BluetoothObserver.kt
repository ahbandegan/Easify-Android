package ir.amirhesambandegan.easify_bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart

/**
 * Monitors the Bluetooth adapter state (Enabled/Disabled).
 */
class BluetoothObserver(private val context: Context) {

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val adapter = bluetoothManager?.adapter

    /**
     * A [Flow] that emits true if Bluetooth is enabled, false otherwise.
     */
    val isBluetoothEnabled: Flow<Boolean> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    trySend(state == BluetoothAdapter.STATE_ON)
                }
            }
        }

        context.registerReceiver(receiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
        
        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }
        .onStart { emit(adapter?.isEnabled == true) }
        .distinctUntilChanged()
}
