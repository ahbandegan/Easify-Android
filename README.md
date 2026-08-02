# EasifyAndroid 🚀

[![](https://jitpack.io/v/ahbandegan/Easify-Android.svg)](https://jitpack.io/#ahbandegan/Easify-Android)

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

### Installation with JitPack
To use EasifyAndroid from JitPack, add the repository and dependency in your Gradle setup.

#### Step 1: Add JitPack to your repositories
In your `settings.gradle.kts`, add JitPack at the end of the repositories block:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

#### Step 2: Add the dependency
In your app module `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.ahbandegan:Easify-Android:Tag")
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

Explore each module in depth with detailed API references and code examples:

### Core Modules
- [**Network**](./app/src/main/java/ir/amirhesambandegan/easify_network/README.md): Safe Ktor requests and file uploads.
- [**Security**](./app/src/main/java/ir/amirhesambandegan/easify_security/README.md): Encryption and Screen Shield.
- [**Permissions**](./app/src/main/java/ir/amirhesambandegan/easify_permission/README.md): Automatic Compose permissions.
- [**Biometric**](./app/src/main/java/ir/amirhesambandegan/easify_biometric/README.md): Fingerprint/Face ID made simple.

### Localization (Iran)
- [**Persian Kit**](./app/src/main/java/ir/amirhesambandegan/easify_persian/README.md): Jalali date, digits, and currency text.
- [**Fintech**](./app/src/main/java/ir/amirhesambandegan/easify_fintech/README.md): National ID, Bank Cards, and SHEBA.

### Hardware & OS
- [**Bluetooth**](./app/src/main/java/ir/amirhesambandegan/easify_bluetooth/README.md): Easy BLE scanning.
- [**Sensors**](./app/src/main/java/ir/amirhesambandegan/easify_sensor/README.md): Accelerometer, Proximity, etc.
- [**Location**](./app/src/main/java/ir/amirhesambandegan/easify_location/README.md): Live coordinates tracking.

### UI & UX
- [**UI Modifiers**](./app/src/main/java/ir/amirhesambandegan/easify_ui/README.md): Bounce, Shimmer, Spacers.
- [**Form State**](./app/src/main/java/ir/amirhesambandegan/easify_form/README.md): Reactive form management.
- [**Haptics**](./app/src/main/java/ir/amirhesambandegan/easify_haptic/README.md): Tactile feedback.
- [**Notifications**](./app/src/main/java/ir/amirhesambandegan/easify_notification/README.md): Simplified channels and alerts.

### Utilities
- [**Context & Intents**](./app/src/main/java/ir/amirhesambandegan/easify_context/README.md): Clipboard and system actions.
- [**Storage**](./app/src/main/java/ir/amirhesambandegan/easify_storage/README.md): Secure Preferences DataStore.
- [**File Pickers**](./app/src/main/java/ir/amirhesambandegan/easify_file/README.md): Visual media selection.
- [**Lifecycle**](./app/src/main/java/ir/amirhesambandegan/easify_lifecycle/README.md): Compose-ready lifecycle effects.
- [**Validation**](./app/src/main/java/ir/amirhesambandegan/easify_validation/README.md): Common regex collection.
- [**Image Processing**](./app/src/main/java/ir/amirhesambandegan/easify_image/README.md): Resize and compress bitmaps.
- [**Formatters**](./app/src/main/java/ir/amirhesambandegan/easify_format/README.md): Numbers and durations.

---

## 🤝 Contribution
Contributions are welcome! If you have ideas for new "Easify" modules, please open an issue or a pull request.

---

## ⚖️ License
MIT License. Free for personal and commercial use.
