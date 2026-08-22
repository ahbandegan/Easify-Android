package ir.amirhesambandegan.easify_bluetooth

import android.bluetooth.BluetoothManager
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import ir.amirhesambandegan.easify_permission.RequestMultiplePermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * A launcher to manage Bluetooth operations in Compose seamlessly.
 * 
 * @property scanner The [BluetoothScanner] instance used for discovering devices.
 * @property observer The [BluetoothObserver] instance used for monitoring Bluetooth state.
 * @property onPermissionDenied Callback invoked when required Bluetooth permissions are denied by the user.
 * @property coroutineScope The [CoroutineScope] used for managing internal asynchronous tasks like scanning.
 */
class BluetoothLauncher(
    private val scanner: BluetoothScanner,
    private val observer: BluetoothObserver,
    private val onPermissionDenied: () -> Unit,
    private val coroutineScope: CoroutineScope
) {
    /**
     * Flow of discovered devices, proxying the scanner's results.
     */
    val scannedDevices: Flow<List<EasifyBluetoothDevice>> = scanner.scannedDevices

    /**
     * Flow of Bluetooth adapter status (enabled/disabled).
     */
    val isEnabled: Flow<Boolean> = observer.isBluetoothEnabled

    /**
     * Internal mutable state tracking if a scan is currently active.
     */
    private val _isScanning = MutableStateFlow(false)
    
    /**
     * Public observable state representing if a scan is currently active.
     */
    val isScanning: StateFlow<Boolean> = _isScanning

    /**
     * Internal state used to trigger the permission request UI flow.
     */
    internal var permissionRequested by mutableStateOf(false)
    
    /**
     * Pending service UUIDs stored while waiting for permissions to be granted.
     */
    internal var pendingServiceUuids: List<UUID>? = null
    
    /**
     * Pending name prefix stored while waiting for permissions to be granted.
     */
    internal var pendingNamePrefix: String? = null

    /**
     * Reference to the ongoing scan coroutine job.
     */
    private var scanJob: Job? = null

    /**
     * Initiates the Bluetooth flow: Permissions -> Enable Bluetooth -> Scan.
     * 
     * @param serviceUuids Filter discovered devices by BLE Service UUID.
     * @param namePrefix Filter discovered devices by name prefix (e.g., "Easify_").
     */
    fun startScanning(serviceUuids: List<UUID>? = null, namePrefix: String? = null) {
        if (_isScanning.value) return
        pendingServiceUuids = serviceUuids
        pendingNamePrefix = namePrefix
        permissionRequested = true
    }

    /**
     * Stops the ongoing BLE scan.
     */
    fun stopScanning() {
        scanJob?.cancel()
        scanJob = null
        _isScanning.value = false
    }

    /**
     * Internal method to actually execute the scan once prerequisites are met.
     */
    internal fun executeScan() {
        if (_isScanning.value) return
        _isScanning.value = true
        scanner.clearDevices()
        scanJob = coroutineScope.launch {
            scanner.startScan(pendingServiceUuids, pendingNamePrefix).collect {
                // Keep collecting
            }
        }
    }
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
    val coroutineScope = rememberCoroutineScope()
    val scanner = remember { BluetoothScanner(context) }
    val observer = remember { BluetoothObserver(context) }
    val launcher = remember { BluetoothLauncher(scanner, observer, onPermissionDenied, coroutineScope) }
    val isBtEnabled by observer.isBluetoothEnabled.collectAsState(initial = false)

    val enableBtLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // After enabling, the observer will pick up the change
    }

    // Auto-execute scan when BT becomes enabled if we requested a scan
    LaunchedEffect(isBtEnabled) {
        if (isBtEnabled && launcher.permissionRequested) {
            launcher.permissionRequested = false
            launcher.executeScan()
        }
    }

    if (launcher.permissionRequested) {
        RequestMultiplePermissions(
            permissions = BluetoothUtils.getRequiredPermissions(),
            onGrant = {
                val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
                val bluetoothAdapter = bluetoothManager?.adapter
                
                if (bluetoothAdapter?.isEnabled == true) {
                    launcher.permissionRequested = false
                    launcher.executeScan()
                } else {
                    enableBtLauncher.launch(Intent(android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE))
                }
            },
            onDenied = {
                launcher.permissionRequested = false
                onPermissionDenied()
            }
        )
    }

    // Cleanup when component leaves composition
    DisposableEffect(launcher) {
        onDispose {
            launcher.stopScanning()
        }
    }

    return launcher
}
