# EasifyAndroid 🚀

[![](https://jitpack.io/v/ahbandegan/Easify-Android.svg)](https://jitpack.io/#ahbandegan/Easify-Android)

**Official Documentation:** [easify-document.netlify.app](https://easify-document.netlify.app/)

**English Documentation** | [مستندات فارسی](./README-fa.md)

**EasifyAndroid** is a comprehensive, production-ready modular Android library designed to eliminate boilerplate and simplify complex tasks. 

Since version 2.1.0, EasifyAndroid is **100% modular**. You no longer need to import the entire library! You can pick and choose exactly which modules you need in your app.

---

## 🚀 Quick Start (JitPack)

### 1. Add the JitPack repository
Add it to your `settings.gradle.kts` at the end of the `repositories` block:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Add dependencies

#### Option A: Import everything at once
If you want access to the entire suite of features, you can import the umbrella module:
```kotlin
dependencies {
    implementation("com.github.ahbandegan.Easify-Android:easify-android:2.1.0")
}
```

#### Option B: A La Carte (Recommended)
Each module is published independently to keep your app size small. Add only the ones you need to your app-level `build.gradle.kts`:
```kotlin
dependencies {
    // Replace <module-name> with any of the modules below
    implementation("com.github.ahbandegan.Easify-Android:<module-name>:2.1.0")
}
```

> **Note:** The source code is included with the JitPack release! When you `ctrl + click` on any Easify function in Android Studio, you will see the actual Kotlin source code instead of compiled `.class` files. 

---

## 📦 Modules Guide

Below is the detailed guide and documentation for all 20 independent modules. Click to expand!

<details>
<summary><b>1. 🔐 easify-security (Encryption & Screen Shield)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-security:2.1.0")`

Provides Hardware-backed AES-GCM encryption and screen protection to prevent screenshots and screen recording.

```kotlin
// Prevent screenshots in this activity/composable
val shield = rememberScreenShield()
shield.enable()

// Encrypt and decrypt strings easily
val encrypted = EasifyCrypto.encrypt("my_secret_text")
val decrypted = EasifyCrypto.decrypt(encrypted)
```
</details>

<details>
<summary><b>2. 🛡️ easify-permission (Compose Permissions)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-permission:2.1.0")`

Automated, lifecycle-aware permission handling for Jetpack Compose without the boilerplate.

```kotlin
val permissionLauncher = rememberEasifyPermission(
    permission = Manifest.permission.CAMERA,
    onGranted = { /* Camera ready */ },
    onDenied = { /* Show rationale */ }
)

Button(onClick = { permissionLauncher.launch() }) {
    Text("Request Camera")
}
```
</details>

<details>
<summary><b>3. 👆 easify-biometric (Fingerprint/Face ID)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-biometric:2.1.0")`

Plug-and-play biometric authentication launcher.

```kotlin
val biometric = rememberBiometricLauncher(
    title = "Verify Identity",
    subtitle = "Use fingerprint to continue",
    onSuccess = { /* Login success */ },
    onError = { error -> /* Handle error */ }
)

Button(onClick = { biometric.authenticate() }) {
    Text("Login with Biometrics")
}
```
</details>

<details>
<summary><b>4. 🌐 easify-network (Ktor Wrapper & File Uploads)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-network:2.1.0")`

Fail-safe Ktor 3.x wrapper with `EasifyResult`, auto-token injection, and effortless multi-part file uploads.

```kotlin
// Simple GET request mapped to a Result class
val result: EasifyResult<UserResponse> = EasifyNetwork.get("https://api.example.com/user")

// Upload file with progress
EasifyNetwork.uploadMultipart(
    url = "https://api.example.com/upload",
    file = myFile,
    onProgress = { percent -> println("Upload: $percent%") }
)
```
</details>

<details>
<summary><b>5. 🇮🇷 easify-persian (Jalali Calendar & Digits)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-persian:2.1.0")`

Jalali (Persian) calendar converter, Persian digits, and number-to-text (Currency) conversion.

```kotlin
// Convert standard date to Jalali
val jalali = Date().toJalaliString() // Output: 1403/05/24

// Convert English numbers to Persian
val text = "Price: 1500".toPersianDigits() // Output: Price: ۱۵۰۰

// Number to Persian text
val words = 1500000L.toPersianWords() // Output: یک میلیون و پانصد هزار
```
</details>

<details>
<summary><b>6. 💳 easify-fintech (Iran Banks & Validation)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-fintech:2.1.0")`

Iranian National ID validator, Bank Card identification (BINs), and SHEBA (IBAN) validation.

```kotlin
// Validate Iranian National ID
val isValid = "0012345678".isValidIranianNationalId()

// Get Bank info from card number
val bankName = BankCardUtils.getBankName("6037991234567890") // Output: Bank Melli

// Validate SHEBA
val isValidSheba = "IR123...".isValidSheba()
```
</details>

<details>
<summary><b>7. 📳 easify-sensor (Hardware Sensors)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-sensor:2.1.0")`

Reactive, descriptive access to hardware sensors (Accelerometer, Proximity, etc.) as Compose State.

```kotlin
val accelerometer by rememberAccelerometerState()
Text("X: ${accelerometer.x}, Y: ${accelerometer.y}, Z: ${accelerometer.z}")

val isClose by rememberProximityState()
if (isClose) { Text("Phone is near your ear!") }
```
</details>

<details>
<summary><b>8. 🔵 easify-bluetooth (BLE Scanner)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-bluetooth:2.1.0")`

Modern BLE scanner and manager that automatically handles Android 12+ permission complexities.

```kotlin
val btScanner = rememberBluetoothScanner()
val devices by btScanner.devices.collectAsState(initial = emptyList())

Button(onClick = { btScanner.startScanning() }) {
    Text("Scan BLE Devices")
}
```
</details>

<details>
<summary><b>9. 📍 easify-location (Live GPS)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-location:2.1.0")`

One-line live location tracking with automatic permission management.

```kotlin
val locationState by rememberLocationTracker()

locationState?.let { loc ->
    Text("Lat: ${loc.latitude}, Lng: ${loc.longitude}")
}
```
</details>

<details>
<summary><b>10. 🎨 easify-ui (Compose Modifiers & Utils)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-ui:2.1.0")`

iOS-style bounce clicks, shimmer loading effects, and keyboard hiding utilities.

```kotlin
// iOS-style bounce click effect
Card(modifier = Modifier.bounceClick { /* action */ }) { ... }

// Shimmer loading effect
Box(modifier = Modifier.size(100.dp).shimmer(isLoading = true))

// Hide keyboard on outside tap
Column(modifier = Modifier.hideKeyboardOnTapOutside()) { ... }
```
</details>

<details>
<summary><b>11. 📝 easify-form (Form State Management)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-form:2.1.0")`

Reactive form state management with built-in validation support.

```kotlin
val form = rememberEasifyForm()
val emailState = form.textField(name = "email", validators = listOf(EmailValidator()))

OutlinedTextField(
    value = emailState.value,
    onValueChange = emailState::onChange,
    isError = emailState.hasError
)

Button(onClick = { if(form.validate()) { /* submit */ } }) { Text("Submit") }
```
</details>

<details>
<summary><b>12. 📳 easify-haptic (Tactile Feedback)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-haptic:2.1.0")`

Semantic tactile feedback (Success/Error/Click) with a single command.

```kotlin
val haptic = LocalHapticFeedback.current

Button(onClick = { 
    // Triggers a distinct "success" vibration pattern
    haptic.performEasifyHaptic(EasifyHapticType.SUCCESS) 
}) {
    Text("Complete Task")
}
```
</details>

<details>
<summary><b>13. 🔔 easify-notification (Simple Alerts)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-notification:2.1.0")`

Simplified Notification Channels and local push alerts.

```kotlin
EasifyNotification.show(
    context = context,
    title = "Download Complete",
    message = "Your file has been downloaded successfully.",
    channelId = "downloads"
)
```
</details>

<details>
<summary><b>14. 📂 easify-file (Visual Pickers)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-file:2.1.0")`

Visual media selection for Images, Videos, and Files.

```kotlin
val filePicker = rememberEasifyFilePicker(
    type = FileType.IMAGE,
    onResult = { uri -> /* Image selected */ }
)

Button(onClick = { filePicker.launch() }) { Text("Pick Image") }
```
</details>

<details>
<summary><b>15. 🗄️ easify-storage (Secure DataStore)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-storage:2.1.0")`

Combines Preferences DataStore with `easify-security` for a secure storage solution.

```kotlin
val storage = rememberEasifyStorage()

// Saved encrypted automatically
storage.saveSecure("auth_token", "jwt_abc123")

// Retrieved and decrypted automatically
val token = storage.getSecure("auth_token")
```
</details>

<details>
<summary><b>16. ⚙️ easify-context (Intents & System)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-context:2.1.0")`

Utilities for Clipboard, Sharing, and System Settings.

```kotlin
context.copyToClipboard("Text to copy")
context.shareText("Check out this app!")
context.openAppSettings()
```
</details>

<details>
<summary><b>17. 🔄 easify-lifecycle (Compose Lifecycle)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-lifecycle:2.1.0")`

Compose-ready lifecycle effect wrappers.

```kotlin
useLifecycleEvent { event ->
    when(event) {
        Lifecycle.Event.ON_RESUME -> { /* App is active */ }
        Lifecycle.Event.ON_PAUSE -> { /* App in background */ }
        else -> {}
    }
}
```
</details>

<details>
<summary><b>18. ✅ easify-validation (Regex Checks)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-validation:2.1.0")`

Common Regex patterns and string validation collection.

```kotlin
val emailValid = "test@test.com".isEmailValid()
val phoneValid = "+989123456789".isPhoneNumberValid()
val passwordStrong = "Aa!123456".isStrongPassword()
```
</details>

<details>
<summary><b>19. 🖼️ easify-image (Bitmap Processing)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-image:2.1.0")`

Resize and compress bitmaps easily before uploading.

```kotlin
val optimizedBitmap = originalBitmap.compress(
    maxWidth = 1080,
    quality = 80
)
```
</details>

<details>
<summary><b>20. 🔢 easify-format (String Formatters)</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-format:2.1.0")`

Clean formatters for Numbers and Durations.

```kotlin
val price = 1500000.toCurrencyFormat() // Output: "1,500,000"
val time = 3661000L.formatDuration()   // Output: "01:01:01"
```
</details>

---

## 🤝 Contribution
Contributions are welcome! If you have ideas for new "Easify" modules, please open an issue or a pull request.

## ⚖️ License
MIT License. Free for personal and commercial use.
