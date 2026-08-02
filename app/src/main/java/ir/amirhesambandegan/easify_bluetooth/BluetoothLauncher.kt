package ir.amirhesambandegan.easify_bluetooth

import android.bluetooth.BluetoothManager
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import ir.amirhesambandegan.easify_permission.RequestMultiplePermissions
import kotlinx.coroutines.flow.Flow

/**
 * A launcher to manage Bluetooth operations in Compose.
 */
class BluetoothLauncher(
    private val scanner: BluetoothScanner,
    private val observer: BluetoothObserver,
    private val onPermissionDenied: () -> Unit
) {
    /**
     * Flow of discovered devices.
     */
    val scannedDevices: Flow<List<EasifyBluetoothDevice>> = scanner.scannedDevices

    /**
     * Flow of Bluetooth adapter status.
     */
    val isEnabled: Flow<Boolean> = observer.isBluetoothEnabled

    /**
     * Internal state to trigger permission request.
     */
    internal var permissionRequested by mutableStateOf(false)

    /**
     * Initiates the Bluetooth flow: Permissions -> Enable Bluetooth -> Scan.
     */
    fun askAndScan() {
        permissionRequested = true
    }

    /**
     * Internal method to start the scan once prerequisites are met.
     */
    internal fun startInternalScan(): Flow<Unit> = scanner.startScan()
}

/**
 * Creates and remembers a [BluetoothLauncher].
 *
 * @param onPermissionDenied Callback when required Bluetooth permissions are denied.
 * @return A [BluetoothLauncher] instance.
 */
@Composable
fun rememberBluetoothLauncher(
    onPermissionDenied: () -> Unit = {}
): BluetoothLauncher {
    val context = LocalContext.current
    val scanner = remember { BluetoothScanner(context) }
    val observer = remember { BluetoothObserver(context) }
    val launcher = remember { BluetoothLauncher(scanner, observer, onPermissionDenied) }

    val enableBtLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // After enabling, the observer will pick up the change
    }

    if (launcher.permissionRequested) {
        RequestMultiplePermissions(
            permissions = BluetoothUtils.getRequiredPermissions(),
            onGrant = {
                launcher.permissionRequested = false
                val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
                val bluetoothAdapter = bluetoothManager?.adapter
                if (bluetoothAdapter?.isEnabled == false) {
                    enableBtLauncher.launch(Intent(android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE))
                }
            },
            onDenied = {
                launcher.permissionRequested = false
                onPermissionDenied()
            }
        )
    }

    return launcher
}
