# Easify Bluetooth

A simplified Bluetooth Low Energy (BLE) management module for Jetpack Compose.

## Key Features

- **BLE Scanning**: Easy device discovery with `BluetoothScanner`.
- **Permission Handling**: Automatically manages Bluetooth permissions across different Android versions.
- **Adapter Monitoring**: Observe the Bluetooth adapter's state (Enabled/Disabled) using Kotlin Flows.
- **Compose Integration**: Seamlessly use Bluetooth features in your UI with `rememberBluetoothLauncher`.

## Usage

### 1. Initialize Bluetooth Launcher
In your Composable, use `rememberBluetoothLauncher` to manage permissions and scanning.

```kotlin
val bluetoothLauncher = rememberBluetoothLauncher(
    onPermissionDenied = {
        // Handle permission denial
    }
)

val devices by bluetoothLauncher.scannedDevices.collectAsState(initial = emptyList())
val isEnabled by bluetoothLauncher.isEnabled.collectAsState(initial = false)

Column {
    if (!isEnabled) {
        Text("Please enable Bluetooth")
    }
    
    Button(onClick = { bluetoothLauncher.askAndScan() }) {
        Text("Start Scanning")
    }

    LazyColumn {
        items(devices) { device ->
            Text("${device.name ?: "Unknown"} - ${device.address}")
        }
    }
}
```

### 2. Manual Scanning
You can also use `BluetoothScanner` directly if you are not using the provided launcher.

```kotlin
val scanner = BluetoothScanner(context)
scope.launch {
    scanner.startScan().collect {
        // Scan started
    }
}

val deviceListFlow = scanner.scannedDevices
```

### 3. Monitoring Bluetooth State
Use `BluetoothObserver` to monitor the system Bluetooth adapter.

```kotlin
val observer = BluetoothObserver(context)
val isEnabledFlow = observer.isBluetoothEnabled
```

## Classes and Components

- `BluetoothScanner`: Core logic for discovering BLE devices.
- `BluetoothLauncher`: Orchestrates permissions, adapter status, and scanning in Compose.
- `BluetoothObserver`: Broadcast receiver wrapper to monitor Bluetooth status.
- `EasifyBluetoothDevice`: A clean data class representing a discovered device.
- `BluetoothUtils`: Utilities for permissions and opening system settings.
