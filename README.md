# EasifyAndroid 🚀

**EasifyAndroid** is a comprehensive, production-ready Android library designed to eliminate boilerplate and simplify complex tasks. From reactive hardware sensors and Bluetooth to secure storage and localized Persian utilities, EasifyAndroid provides a clean, modern API for every Android developer.

---

## 📦 Modules & Features

### 🔐 Security & Permissions
- **Easify-Security**: Hardware-backed AES-GCM encryption and screen protection (screenshot/recording shield).
- **Easify-Permission**: Automated, lifecycle-aware permission handling for Jetpack Compose.
- **Easify-Biometric**: Plug-and-play biometric authentication launcher.

### 🌐 Networking
- **Easify-Network**: Fail-safe Ktor 3.x wrapper with `EasifyResult`, auto-token injection, and effortless multi-part file uploads with progress tracking.
- **Connectivity-Observer**: Real-time internet monitoring with Kotlin Flow.

### 🇮🇷 Persian Kit (Localization)
- **Easify-Persian**: Jalali (Persian) calendar converter, Persian digits, and number-to-text (Currency) conversion.
- **Easify-Fintech**: Iranian National ID validator, Bank Card identification (BINs), and SHEBA (IBAN) validation.

### 📱 Hardware & OS
- **Easify-Sensor**: Reactive, descriptive access to all hardware sensors (Accelerometer, Proximity, etc.).
- **Easify-Bluetooth**: Modern BLE scanner and manager that handles Android 12+ permission complexity.
- **Easify-Location**: One-line live location tracking with automatic permission management.

### 🎨 UI & UX
- **Easify-Modifiers**: iOS-style `.bounceClick()`, skeleton `.shimmer()`, and `.hideKeyboardOnTapOutside()`.
- **Easify-Form**: Reactive form state management with built-in validation support.
- **Easify-Haptic**: Semantic tactile feedback (Success/Error/Click) with a single command.

---

## 🚀 Quick Start

### Installation
Add the following to your `build.gradle.kts` (Maven Central / JitPack setup expected):

```kotlin
dependencies {
    implementation("ir.amirhesambandegan:easify-android:1.0.0")
}
```

### Example: Secure Data Storage
```kotlin
val controller = DataStoreController(context)
controller.saveSecure(PreferenceKeys.stringKey("token"), "secret_123")
```

### Example: Reactive Bluetooth Scanning
```kotlin
val bt = rememberBluetoothLauncher()
val devices by bt.scannedDevices.collectAsState(emptyList())

Button(onClick = { bt.askAndScan() }) {
    Text("Find Devices")
}
```

---

## 📖 Detailed Documentation
Each module contains its own dedicated `README.md` with deep-dives and code samples:
- [Network Docs](./app/src/main/java/ir/amirhesambandegan/easify_network/README.md)
- [Security Docs](./app/src/main/java/ir/amirhesambandegan/easify_security/README.md)
- [Persian Kit Docs](./app/src/main/java/ir/amirhesambandegan/easify_persian/README.md)
- ... and more in the source folders.

---

## 🤝 Contribution
Contributions are welcome! If you have ideas for new "Easify" modules, please open an issue or a pull request.

---

## ⚖️ License
MIT License. Free for personal and commercial use.
