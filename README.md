# EasifyAndroid 🚀

[![](https://jitpack.io/v/ahbandegan/Easify-Android.svg)](https://jitpack.io/#ahbandegan/Easify-Android)

**Official Documentation:** [https://ahbandegan.github.io/Easify-Android-docuement](https://ahbandegan.github.io/Easify-Android-docuement)

**English Documentation** | [مستندات فارسی](./README-fa.md)

**EasifyAndroid** is a comprehensive, production-ready modular **Kotlin Multiplatform (KMP)**, **Compose Multiplatform (CMP)**, and **Android** toolkit designed to eliminate boilerplate across UI, state management, forms, networking, Persian/Fintech logic, cryptography, and device APIs.

Starting with **v3.1.0**, the library is organized into **32 independent modules**:
- **9 Pure Kotlin Multiplatform / Console Core modules** (zero Android or Compose dependencies — run on Pure Kotlin Console JVM apps, Native, JS/Wasm, Ktor backends, CLI tools, and shared KMP logic)
- **8 Compose Multiplatform UI modules** (Android, iOS, Desktop JVM, JS, and WasmJS)
- **14 Android platform modules** (hardware-backed Google Tink AEAD & Streaming AEAD security, Credential Manager, CameraX, ML Kit, BLE, Location, Sensors, Biometrics, and more)
- **1 Umbrella module** (`easify-android`) for single-dependency Android setup

---

## ✨ What's New in 3.1.0

- **9 Pure Kotlin Console / KMP Core Modules:** `easify-validation`, `easify-format-core`, `easify-persian-core`, `easify-fintech-core`, `easify-state-core`, `easify-form-core`, `easify-network-core`, `easify-auth-core`, `easify-location-core`
- **8 Compose Multiplatform UI Modules:** `easify-ui`, `easify-state`, `easify-form`, `easify-format`, `easify-persian`, `easify-fintech`, `easify-lifecycle`, `easify-image`
- **`SuspendButton` Customization:** Added `shape: Shape? = null`, `colors: ButtonColors? = null`, and `contentPadding: PaddingValues` plus single-flight concurrency protection
- **Security Hardening:** Replaced deprecated `security-crypto` with **Google Tink (`1.24.0`)** AEAD (`AES256_SIV` + `AES256_GCM`) in `easify-security` and **Streaming AEAD (`AES256_GCM_HKDF_4KB`)** in `easify-storage`; constant-time OAuth `state` verification in `easify-auth-core`; ZIP-slip protection & sanitized filenames in `easify-file`/`easify-network`; safe HTTPS `openBrowser` validation
- **Modern Stack & Performance:** Kotlin `2.4.21`, AGP `9.5.0` (`compileSdk = 37`), Gradle `9.9.1`, Compose Multiplatform `1.12.2`, Ktor `3.6.1`, Coroutines `1.11.0`, Jetpack Compose BOM `2026.11.00`, `Modifier.Node` custom modifiers, and cancellation-safe structured concurrency

---

## 🚀 Quick Start (JitPack)

### 1. Add the JitPack repository
Add JitPack to `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Add dependencies

#### Option A: Import all modules on Android
```kotlin
dependencies {
    implementation("com.github.ahbandegan.Easify-Android:easify-android:3.1.0")
}
```

#### Option B: Modular Pick & Choose (Android, CMP, or Pure Kotlin Console)
```kotlin
dependencies {
    // Pure Kotlin Console / Backend / KMP commonMain
    implementation("com.github.ahbandegan.Easify-Android:easify-validation:3.1.0")
    implementation("com.github.ahbandegan.Easify-Android:easify-persian-core:3.1.0")
    implementation("com.github.ahbandegan.Easify-Android:easify-fintech-core:3.1.0")

    // Compose Multiplatform UI
    implementation("com.github.ahbandegan.Easify-Android:easify-ui:3.1.0")
    implementation("com.github.ahbandegan.Easify-Android:easify-state:3.1.0")

    // Android-specific
    implementation("com.github.ahbandegan.Easify-Android:easify-security:3.1.0")
}
```

> **Note:** Sources JARs are published alongside every release artifact so IDE go-to-definition (`Ctrl + Click`) opens the readable Kotlin source code.

---

## 💻 Pure Kotlin Console / Server Example

```kotlin
import ir.amirhesambandegan.easify_validation.isValidEmail
import ir.amirhesambandegan.easify_persian.JalaliConverter
import ir.amirhesambandegan.easify_fintech.getBankName
import ir.amirhesambandegan.easify_format.toCurrencyString
import ir.amirhesambandegan.easify_form.Validators
import ir.amirhesambandegan.easify_form.buildForm
import ir.amirhesambandegan.easify_location.GeoPoint
import ir.amirhesambandegan.easify_location.LocationMath

fun main() {
    println("Email valid: ${"dev@easify.ir".isValidEmail()}")
    println("Jalali: ${JalaliConverter.gregorianToJalali(2026, 3, 21).toReadablePersianDate()}")
    println("Bank: ${"6037991234567890".getBankName()}")
    println("Total: ${1_500_000L.toCurrencyString(symbol = "IRR", decimals = 0)}")

    val tehran = GeoPoint(35.6892, 51.3890)
    val shiraz = GeoPoint(29.5918, 52.5837)
    println("Distance km: ${LocationMath.distanceBetween(tehran, shiraz) / 1000f}")

    val form = buildForm {
        field("email", initialValue = "dev@easify.ir", rules = listOf(Validators.email()))
    }
    println("Form valid: ${form.validateAll()}")
}
```

---

## 📦 Modules Directory (32 Modules)

### Pure Kotlin Multiplatform / Console Core (9 Modules)

| Module | Targets | Description |
| :--- | :--- | :--- |
| **`easify-validation`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | Zero-dependency Regex validators, `PasswordPolicy`, National ID, Luhn, IPv4/IPv6, UUID |
| **`easify-format-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | Currency, compact numbers, casing, extractors, masking, Levenshtein similarity, `toTimeAgo` |
| **`easify-persian-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | `JalaliDate`, `JalaliConverter`, Persian/English digit & letter normalization, words & validators |
| **`easify-fintech-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | Iranian bank card BINs, SHEBA/IBAN, `BillValidator`, `ShamsiDate`, pricing & tax helpers |
| **`easify-state-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | Pure Kotlin `Bloc`, `Cubit`, `EffectBloc`, `Transformers` (`sequential`, `restartable`, `droppable`, `concurrent`) |
| **`easify-form-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | Headless `buildForm`, `CoreFormState`, `Validators`, `InputFilters`, sync & async validation |
| **`easify-network-core`** | Android, JVM, Apple, Linux, JS, WasmJS | Multiplatform `createKtorClient()`, `safeRequest`, `retryNetworkCall`, `EasifyResult`, `CurlLoggerPlugin`, `downloadToSink` |
| **`easify-auth-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | `PkceHelper` (RFC 7636), `OtpExtractor`, `OAuthProvider`, `OAuthConfig`, `AuthResult` |
| **`easify-location-core`** | Android, JVM, Apple, Linux, Mingw, JS, WasmJS | `GeoPoint`, `LocationMath` (distance, bearing, geofence), `filterPointJumps`, DMS & ETA |

---

### Modules Guide & Code Snippets

<details>
<summary><b>1. 🔐 easify-security (Google Tink Encryption & Screen Shield) — Android</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-security:3.1.0")`

Provides Google Tink (`AES256_SIV` + `AES256_GCM`) encrypted preferences, Android Keystore `EncryptionManager`, and screen/background/tapjacking shields.

```kotlin
// Prevent screenshots & background previews in Compose
ScreenShield()
BackgroundPrivacyShield()

PreventTapjacking {
    // Sensitive UI protected from overlay touches
}

// Tink-backed Encrypted SharedPreferences
val prefs = SecureStorage.getEncryptedPreferences(context, "vault_prefs")
prefs.edit().putString("api_key", "secret_value").apply()

// Hardware-backed Keystore AES-256-GCM encryption
val encrypted = EncryptionManager.encrypt(alias = "session_key", plainText = "my_secret_text")
val decrypted = EncryptionManager.decrypt(alias = "session_key", encryptedText = encrypted)
```
</details>

<details>
<summary><b>2. 🗄️ easify-storage (DataStore & Tink Streaming AEAD) — Android</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-storage:3.1.0")`

Encrypted Preferences DataStore (`DataStoreController`), Google Tink Streaming AEAD (`SecureFileStorage`), TTL cache (`SmartCacheManager`), JSON object mapping, and ZIP backups.

```kotlin
val controller = DataStoreController(context)
val tokenKey = PreferenceKeys.stringKey("auth_token")

// Save and read AES-encrypted values in DataStore
controller.saveSecure(tokenKey, "jwt_abc123")
val token = controller.getSecure(tokenKey)

// Encrypt files using Google Tink Streaming AEAD (AES256_GCM_HKDF_4KB)
val secretFile = File(context.filesDir, "report.enc")
SecureFileStorage.writeSecureString(context, secretFile, "Confidential report")
val restored = SecureFileStorage.readSecureString(context, secretFile)
```
</details>

<details>
<summary><b>3. 🎨 easify-ui (Compose Multiplatform UI & Theme Reveals) — CMP</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-ui:3.1.0")`

Cross-platform Material 3 components and `Modifier.Node` effects: customizable `SuspendButton` (`shape`, `colors`, `contentPadding`), 7 theme transition reveal styles, swipe actions, OTP input, glassmorphism, shimmer, shake, bounce click.

```kotlin
var isDark by remember { mutableStateOf(false) }

EasifyThemeContainer(
    isDark = isDark,
    revealType = ThemeRevealType.CIRCULAR,
    anchor = ThemeRevealAnchor.TopEnd,
    lightColors = lightColorScheme(),
    darkColors = darkColorScheme()
) {
    Column(modifier = Modifier.hideKeyboardOnTapOutside()) {
        SuspendButton(
            onClick = { delay(1500) },
            type = SuspendButtonType.FILLED,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Submit Async")
        }

        EasifySwipeToAction(
            onSwipeRight = { /* archive */ },
            onSwipeLeft = { /* delete */ }
        ) {
            Text("Swipe me left or right")
        }

        Box(modifier = Modifier.size(100.dp).shimmer(showShimmer = true))
    }
}
```
</details>

<details>
<summary><b>4. ⚡ easify-state & easify-state-core (BLoC & Cubit) — CMP & KMP Core</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-state:3.1.0")` or `easify-state-core:3.1.0`

Full Flutter-style `Bloc`, `Cubit`, `EffectBloc`, `BlocViewModel`, concurrency `Transformers`, `BlocProvider`, `BlocBuilder`, `BlocEffectListener`, `read`, `watch`, and `select`.

```kotlin
data class CounterState(val count: Int = 0) : BlocState
sealed interface CounterEvent : BlocEvent {
    data object Increment : CounterEvent
}

class CounterBloc : Bloc<CounterEvent, CounterState>(CounterState(0)) {
    init {
        on<CounterEvent.Increment>(transformer = Transformers.sequential()) { _, emit ->
            emit(state.value.copy(count = state.value.count + 1))
        }
    }
}

@Composable
fun CounterScreen() {
    val state = watch<CounterBloc, CounterState>()
    val bloc = read<CounterBloc>()

    Column {
        Text("Count: ${state.count}")
        Button(onClick = { bloc.add(CounterEvent.Increment) }) {
            Text("Increment")
        }
    }
}
```
</details>

<details>
<summary><b>5. 📝 easify-form & easify-form-core (Form State) — CMP & KMP Core</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-form:3.1.0")` or `easify-form-core:3.1.0`

Reactive Compose form state (`rememberForm`), `FormArrayState`, async validators, input filters, visual masks, and Android draft persistence (`saveDraft`).

```kotlin
val form = rememberForm {
    field(
        key = "email",
        rules = listOf(Validators.required(), Validators.email()),
        inputFilter = InputFilters.noSpaces()
    )
}
val emailField = form.getField("email")!!

OutlinedTextField(
    value = emailField.value,
    onValueChange = { emailField.value = it },
    isError = emailField.error != null,
    supportingText = emailField.error?.let { { Text(it) } }
)

Button(onClick = { if (form.validateAll()) { /* submit */ } }) {
    Text("Submit")
}
```
</details>

<details>
<summary><b>6. 🌐 easify-network & easify-network-core (Ktor 3.6.1 Wrapper) — Android & KMP Core</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-network:3.1.0")` or `easify-network-core:3.1.0`

Safe Ktor `3.6.1` client builder, `EasifyResult`, retry with exponential backoff, OAuth bearer refresh, redacted `CurlLoggerPlugin`, streaming downloads, multipart uploads, and `ConnectivityObserver`.

```kotlin
val client = createKtorClient(baseUrl = "https://api.example.com")

val result: EasifyResult<UserResponse> = client.safeRequest {
    get("/user")
}

result.onSuccess { user -> println(user.name) }
    .onApiError { code, msg -> println("HTTP $code: $msg") }

// Android offline-aware ConnectivityObserver
val online by ConnectivityObserver(context).online.collectAsState(initial = true)
```
</details>

<details>
<summary><b>7. 🔑 easify-auth & easify-auth-core (Credential Manager, SMS OTP & PKCE) — Android & KMP Core</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-auth:3.1.0")` or `easify-auth-core:3.1.0`

Google Credential Manager sign-in, SMS Retriever & User Consent OTP extraction, and OAuth 2.0 PKCE flows with constant-time state verification.

```kotlin
val authClient = rememberEasifyAuth()

LaunchedEffect(Unit) {
    when (val result = authClient.signInWithGoogle(serverClientId = "YOUR_WEB_CLIENT_ID")) {
        is AuthResult.Success -> println("Welcome ${result.data.displayName}")
        is AuthResult.Error -> println("Auth failed: ${result.throwable.message}")
    }
}

SmsOtpEffect(
    otpLengthRange = 4..6,
    onOtpReceived = { otp -> println("OTP: $otp") }
)

val oauthConfig = OAuthConfig(
    clientId = "YOUR_GITHUB_CLIENT_ID",
    clientSecret = "YOUR_GITHUB_CLIENT_SECRET",
    redirectUri = "myapp://oauth-callback",
    scopes = listOf("read:user", "user:email")
)
```
</details>

<details>
<summary><b>8. 🇮🇷 easify-persian & easify-persian-core (Jalali Calendar & Pickers) — CMP & KMP Core</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-persian:3.1.0")` or `easify-persian-core:3.1.0`

Pure KMP Jalali date math, Persian/English digit conversion, Persian letter normalization, number-to-words, and CMP `PersianDatePickerDialog` / `PersianTimePickerDialog`.

```kotlin
val today = JalaliDate.now()
val formatted = today.format(persianDigits = true)            // ۱۴۰۵/۰۸/۲۱
val readable = today.toReadablePersianDate()                  // پنج‌شنبه ۲۱ آبان ۱۴۰۵
val text = "Price: 1500".toPersianDigits()                    // Price: ۱۵۰۰
val words = 1_500_000L.toPersianWords()                       // یک میلیون و پانصد هزار
val phoneValid = "۰۹۱۲۳۴۵۶۷۸۹".isValidPersianMobile()         // true
```
</details>

<details>
<summary><b>9. 💳 easify-fintech & easify-fintech-core (Iran Banking & UI) — CMP & KMP Core</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-fintech:3.1.0")` or `easify-fintech-core:3.1.0`

Bank card BIN identification, Luhn check, SHEBA (IBAN) validation, Iranian Legal ID & National ID checks, utility bill parser, Compose `VisualTransformation` masks, and `EasifyBalanceText`.

```kotlin
val isValid = "0012345678".isValidNationalId()
val bankName = "6037991234567890".getBankName()          // بانک ملی ایران
val isValidSheba = "IR820540102680020817909002".isValidSheba()

// Compose UI privacy-toggled balance widget
EasifyBalanceText(balance = 25_000_000L, currency = "تومان")
```
</details>

<details>
<summary><b>10. ✅ easify-validation (Pure KMP Regex & Password Policies) — KMP</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-validation:3.1.0")`

Pure Kotlin Multiplatform validation helpers with zero external dependencies beyond Kotlin stdlib.

```kotlin
val emailOk = "test@easify.dev".isValidEmail()
val phoneOk = "+989123456789".isValidMobile(allowInternational = true)
val strongOk = "Aa!123456".isValidStrongPassword()
val policyOk = "Secret12".isValidPassword(PasswordPolicy(minLength = 8, requireSpecialChar = false))
val cardOk = "6037991234567890".isValidCreditCardLuhn()
```
</details>

<details>
<summary><b>11. 🔢 easify-format & easify-format-core (Number, Text & Time Formatting) — CMP & KMP Core</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-format:3.1.0")` or `easify-format-core:3.1.0`

Currency formatting, compact notation (`1.5M`), ordinals, masking, similarity percentage, timer/duration formatters, and Compose `AnnotatedString` highlighting & linkification.

```kotlin
val price = 1500000L.toCurrencyString(symbol = "$", decimals = 0) // "$1,500,000"
val compact = 1_500_000L.toCompactFormat()                        // "1.5M"
val timer = 3661L.toTimerFormat()                                 // "01:01:01"
val duration = 3661L.toReadableDuration()                         // "1h 1m 1s"
```
</details>

<details>
<summary><b>12. 🖼️ easify-image (Zoomable Viewer, Capturable & Bitmap Pipeline) — CMP</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-image:3.1.0")`

Multiplatform pinch-to-zoom viewer (`EasifyZoomableImage`), `ImageCapturable`, palette sampling, plus Android EXIF orientation fix, size-targeted binary search compression, circle crop, blur, and watermarking.

```kotlin
// Compose Multiplatform pinch/double-tap zoom viewer
EasifyZoomableImage(imageBitmap = myImageBitmap)

// Android EXIF-aware bitmap compression to target size in KB
val bitmap = imageUri.toCorrectedBitmap(context)
val compressedBytes = bitmap?.compressToTargetSize(maxSizeKB = 250)
```
</details>

<details>
<summary><b>13. 🔄 easify-lifecycle (Compose Lifecycle & Window Observers) — CMP</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-lifecycle:3.1.0")`

Cross-platform lifecycle effects (`OnResumeEffect`, `OnPauseEffect`, `OnFirstCreateEffect`, `LifecycleTimer`, `LaunchWhenResumed`) plus Android keyboard, foreground, screen, and focus observers.

```kotlin
OnResumeEffect { /* Screen became active */ }
OnPauseEffect { /* Screen paused */ }

LifecycleTimer(intervalMillis = 1_000L) {
    // Runs only while lifecycle is at least RESUMED
}

val isForeground by rememberAppForegroundState()
val keyboard by rememberKeyboardState()
```
</details>

<details>
<summary><b>14. 🛡️ easify-permission (Compose Permissions & Settings) — Android</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-permission:3.1.0")`

State-driven and declarative Compose runtime permission helpers plus overlay & battery optimization utilities.

```kotlin
val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

Button(onClick = { cameraPermission.launchPermissionRequest() }) {
    Text("Status: ${cameraPermission.status}")
}

// Or declarative auto-request composable
RequestPermission(
    permission = Manifest.permission.CAMERA,
    onGrant = { /* granted */ },
    onDenied = { /* denied */ }
)
```
</details>

<details>
<summary><b>15. 👆 easify-biometric (BiometricPrompt, AutoShield & Crypto) — Android</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-biometric:3.1.0")`

One-line `rememberBiometricLauncher`, `BiometricAutoShield`, hardware capability detection, Keystore crypto-bound prompt, and grace period helper.

```kotlin
val biometricLauncher = rememberBiometricLauncher { result ->
    when (result) {
        is BiometricResult.Success -> { /* Unlocked */ }
        is BiometricResult.Error -> println(result.errString)
    }
}

Button(onClick = {
    if (BiometricUtils.canAuthenticate(context)) {
        biometricLauncher?.launch(
            title = "Verify Identity",
            subtitle = "Use fingerprint or face to continue"
        )
    }
}) {
    Text("Authenticate")
}
```
</details>

<details>
<summary><b>16. 📳 easify-haptic (Tactile Patterns & Modifiers) — Android</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-haptic:3.1.0")`

Compose `LocalHapticFeedback` extensions, `Modifier.hapticClickable`, `PullHapticTracker`, and API-safe `VibratorManager` patterns (`hapticConfirm`, `hapticReject`, `hapticHeartbeat`).

```kotlin
val haptic = LocalHapticFeedback.current

Button(onClick = { haptic.performSuccess() }) {
    Text("Complete Task")
}

Box(modifier = Modifier.hapticClickable { context.hapticConfirm() }) {
    Text("Tap for tactile confirmation")
}
```
</details>

<details>
<summary><b>17. 🔔 easify-notification (Channels, WorkManager & In-App Banners) — Android</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-notification:3.1.0")`

Notification channel helper, progress/big-style/direct-reply notifications, `rememberNotificationPermission()`, WorkManager scheduling, and Compose `InAppBanner`.

```kotlin
val notificationPermission = rememberNotificationPermission()

NotificationHelper.showNotification(
    context = context,
    channelId = "downloads",
    channelName = "Downloads",
    title = "Download Complete",
    message = "Your file has been saved successfully.",
    smallIcon = R.drawable.ic_notification
)
```
</details>

<details>
<summary><b>18. 📂 easify-file (Pickers, MediaStore, ZIP & Hashing) — Android</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-file:3.1.0")`

PhotoPicker/Document/Directory pickers, `MediaStore` Gallery/Downloads exporters, hardened ZIP archive utilities (with ZIP-slip guard), and SHA-256/MD5 helpers.

```kotlin
var pickImage by remember { mutableStateOf(false) }

if (pickImage) {
    FilePicker(mediaType = MediaType.ImageOnly) { uri ->
        pickImage = false
        val name = uri?.getFileName(context)
    }
}
```
</details>

<details>
<summary><b>19. ⚙️ easify-context (Intents, System Services & Settings) — Android</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-context:3.1.0")`

Concise `Context` extension helpers for clipboard, safe HTTPS browser intents, email, dialer, system managers, and Android 10+ Settings Panels.

```kotlin
context.copyToClipboard("Text to copy")
context.shareText("Check out this app!")
context.openBrowser("https://github.com/ahbandegan/Easify-Android")
context.openAppSettings()
```
</details>

<details>
<summary><b>20. 📍 easify-location & easify-location-core (GPS, Anti-Fake & Geo Math) — Android & KMP Core</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-location:3.1.0")` or `easify-location-core:3.1.0`

Fused location flow (`LocationTracker`), Compose geofencing, GPS resolution prompt, mock-GPS detection, speed-jump filtering, and pure KMP `GeoPoint` / `LocationMath`.

```kotlin
val locationState = rememberLocationTracker()
val currentLocation by locationState.tracker
    .getLocationUpdates(interval = 5_000L)
    .filterJumps(maxSpeedKmh = 200f)
    .collectAsState(initial = null)

currentLocation?.let { loc ->
    Text("Lat: ${loc.latitude}, Lng: ${loc.longitude}, Mock: ${loc.isMocked()}")
}
```
</details>

<details>
<summary><b>21. 🔵 easify-bluetooth (BLE Scanner, GATT & Advertiser) — Android</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-bluetooth:3.1.0")`

Android 12+ permission-aware `rememberBluetoothLauncher()`, BLE GATT client, peripheral advertiser, RSSI distance estimator, and adapter state Flow.

```kotlin
val btLauncher = rememberBluetoothLauncher()
val devices by btLauncher.scannedDevices.collectAsState(initial = emptyList())

Button(onClick = { btLauncher.startScanning() }) {
    Text("Scan BLE Devices (${devices.size})")
}
```
</details>

<details>
<summary><b>22. 📷 easify-camera (CameraX Preview & ML Kit QR Scanner) — Android</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-camera:3.1.0")`

Lifecycle-bound CameraX `CameraPreview`, coroutine `takePicture()`, torch/flash/lens/zoom controls, and ML Kit `QrCodeScannerView`.

```kotlin
val cameraState = rememberCameraState()
CameraPreview(cameraState = cameraState, modifier = Modifier.fillMaxSize())

// Or instant QR / Barcode scanner
QrCodeScannerView(
    modifier = Modifier.fillMaxSize(),
    onBarcodeDetected = { rawValue -> println("Scanned: $rawValue") }
)
```
</details>

<details>
<summary><b>23. 📳 easify-sensor (Reactive Hardware Sensors) — Android</b></summary>

**Import:** `implementation("com.github.ahbandegan.Easify-Android:easify-sensor:3.1.0")`

Cold `Flow` access to accelerometer, gyroscope, proximity, light, and barometer sensors via `rememberSensorObserver`.

```kotlin
val observer = rememberSensorObserver(Sensor.TYPE_ACCELEROMETER)
val sensorEvent by observer.sensorData.collectAsState(initial = null)

sensorEvent?.toThreeAxisData()?.let { axis ->
    Text("X: ${axis.x}, Y: ${axis.y}, Z: ${axis.z}")
}
```
</details>

---

## 🤝 Contribution
Contributions are welcome! If you have ideas for new modules or improvements, feel free to open an issue or pull request.

## ⚖️ License
Licensed under the [GNU General Public License v3.0 (GPL-3.0)](./LICENSE).
