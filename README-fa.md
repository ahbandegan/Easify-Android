# EasifyAndroid ðŸš€

[![](https://jitpack.io/v/ahbandegan/Easify-Android.svg)](https://jitpack.io/#ahbandegan/Easify-Android)

[English Documentation](./README.md) | **Ù…Ø³ØªÙ†Ø¯Ø§Øª ÙØ§Ø±Ø³ÛŒ**

**EasifyAndroid** ÛŒÚ© Ú©ØªØ§Ø¨Ø®Ø§Ù†Ù‡ Ù‚Ø¯Ø±ØªÙ…Ù†Ø¯ØŒ Ù…Ø§Ú˜ÙˆÙ„Ø§Ø± Ùˆ Ø¢Ù…Ø§Ø¯Ù‡â€ŒÛŒ Ø§Ø³ØªÙØ§Ø¯Ù‡ Ø¨Ø±Ø§ÛŒ Ø§Ù†Ø¯Ø±ÙˆÛŒØ¯ Ø§Ø³Øª Ú©Ù‡ Ø¨Ø±Ø§ÛŒ Ø­Ø°Ù Ú©Ø¯Ù‡Ø§ÛŒ ØªÚ©Ø±Ø§Ø±ÛŒ Ùˆ Ø³Ø§Ø¯Ù‡â€ŒØ³Ø§Ø²ÛŒ Ú©Ø§Ø±Ù‡Ø§ÛŒ Ù¾ÛŒÚ†ÛŒØ¯Ù‡ Ø·Ø±Ø§Ø­ÛŒ Ø´Ø¯Ù‡ Ø§Ø³Øª. 

Ø§Ø² Ù†Ø³Ø®Ù‡ Û±.Û°.Û± Ø¨Ù‡ Ø¨Ø¹Ø¯ØŒ Ø§ÛŒÙ† Ú©ØªØ§Ø¨Ø®Ø§Ù†Ù‡ **Û±Û°Û°Ùª Ù…Ø§Ú˜ÙˆÙ„Ø§Ø±** Ø´Ø¯Ù‡ Ø§Ø³Øª! Ø´Ù…Ø§ Ø¯ÛŒÚ¯Ø± Ù†ÛŒØ§Ø²ÛŒ Ù†Ø¯Ø§Ø±ÛŒØ¯ Ú©Ù„ Ú©ØªØ§Ø¨Ø®Ø§Ù†Ù‡ Ø±Ø§ Ø¨Ù‡ Ù¾Ø±ÙˆÚ˜Ù‡ Ø®ÙˆØ¯ Ø§Ø¶Ø§ÙÙ‡ Ú©Ù†ÛŒØ¯Ø› Ù…ÛŒâ€ŒØªÙˆØ§Ù†ÛŒØ¯ Ø¯Ù‚ÛŒÙ‚Ø§Ù‹ ÙÙ‚Ø· Ø¨Ø®Ø´â€ŒÙ‡Ø§ÛŒÛŒ Ú©Ù‡ Ù†ÛŒØ§Ø² Ø¯Ø§Ø±ÛŒØ¯ Ø±Ø§ Ø§Ù†ØªØ®Ø§Ø¨ Ú©Ù†ÛŒØ¯.

---

## ðŸš€ Ø´Ø±ÙˆØ¹ Ø³Ø±ÛŒØ¹ (JitPack)

### Û±. Ø§Ø¶Ø§ÙÙ‡ Ú©Ø±Ø¯Ù† Ù…Ø®Ø²Ù† JitPack
Ø§ÛŒÙ† Ú©Ø¯ Ø±Ø§ Ø¯Ø± Ø§Ù†ØªÙ‡Ø§ÛŒ Ø¨Ù„Ø§Ú© `repositories` Ø¯Ø± ÙØ§ÛŒÙ„ `settings.gradle.kts` Ù¾Ø±ÙˆÚ˜Ù‡ Ø®ÙˆØ¯ Ø§Ø¶Ø§ÙÙ‡ Ú©Ù†ÛŒØ¯:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Û². Ø§Ø¶Ø§ÙÙ‡ Ú©Ø±Ø¯Ù† ÙˆØ§Ø¨Ø³ØªÚ¯ÛŒâ€ŒÙ‡Ø§ (Dependencies)

#### ðŸŒŸ Ø±ÙˆØ´ Ø§ÙˆÙ„: Ø§Ø¶Ø§ÙÙ‡ Ú©Ø±Ø¯Ù† Ú©Ù„ Ø§Ù…Ú©Ø§Ù†Ø§Øª Ø¨Ù‡ ØµÙˆØ±Øª ÛŒÚ©Ø¬Ø§
Ø§Ú¯Ø± Ù…ÛŒâ€ŒØ®ÙˆØ§Ù‡ÛŒØ¯ Ø¨Ù‡ ØªÙ…Ø§Ù… Ù‚Ø§Ø¨Ù„ÛŒØªâ€ŒÙ‡Ø§ÛŒ Ø§ÛŒÙ† Ú©ØªØ§Ø¨Ø®Ø§Ù†Ù‡ Ø¨Ù‡ ØµÙˆØ±Øª Ù‡Ù…Ø²Ù…Ø§Ù† Ø¯Ø³ØªØ±Ø³ÛŒ Ø¯Ø§Ø´ØªÙ‡ Ø¨Ø§Ø´ÛŒØ¯ØŒ Ú©Ø§ÙÛŒØ³Øª Ù¾Ú©ÛŒØ¬ Ø§ØµÙ„ÛŒ Ø±Ø§ Ø§Ø¶Ø§ÙÙ‡ Ú©Ù†ÛŒØ¯:
```kotlin
dependencies {
    implementation("com.github.ahbandegan.Easify-Android:easify-android:2.0.0")
}
```

#### ðŸŽ¯ Ø±ÙˆØ´ Ø¯ÙˆÙ…: Ø§Ø¶Ø§ÙÙ‡ Ú©Ø±Ø¯Ù† Ù…Ø§Ú˜ÙˆÙ„â€ŒÙ‡Ø§ÛŒ Ø¯Ù„Ø®ÙˆØ§Ù‡
Ø§Ú¯Ø± Ù…ÛŒâ€ŒØ®ÙˆØ§Ù‡ÛŒØ¯ Ø­Ø¬Ù… Ø§Ù¾Ù„ÛŒÚ©ÛŒØ´Ù† Ø´Ù…Ø§ Ú©Ù… Ø¨Ù…Ø§Ù†Ø¯ Ùˆ ÙÙ‚Ø· Ø§Ø² Ú†Ù†Ø¯ Ù‚Ø§Ø¨Ù„ÛŒØª Ø®Ø§Øµ Ø§Ø³ØªÙØ§Ø¯Ù‡ Ú©Ù†ÛŒØ¯ØŒ Ù…ÛŒâ€ŒØªÙˆØ§Ù†ÛŒØ¯ Ø§Ø² Ù„ÛŒØ³Øª Ù¾Ø§ÛŒÛŒÙ†ØŒ ÙÙ‚Ø· Ù…Ø§Ú˜ÙˆÙ„â€ŒÙ‡Ø§ÛŒ Ù…ÙˆØ±Ø¯ Ù†ÛŒØ§Ø²ØªØ§Ù† Ø±Ø§ Ø§Ø¶Ø§ÙÙ‡ Ú©Ù†ÛŒØ¯:
```kotlin
dependencies {
    // Ø¨Ù‡ Ø¬Ø§ÛŒ <module-name> Ù†Ø§Ù… ÛŒÚ©ÛŒ Ø§Ø² Ù…Ø§Ú˜ÙˆÙ„â€ŒÙ‡Ø§ÛŒ Ù„ÛŒØ³Øª Ø²ÛŒØ± Ø±Ø§ Ù‚Ø±Ø§Ø± Ø¯Ù‡ÛŒØ¯
    implementation("com.github.ahbandegan.Easify-Android:<module-name>:2.0.0")
}
```

> **Ù†Ú©ØªÙ‡:** Ø³ÙˆØ±Ø³ Ú©Ø¯Ù‡Ø§ÛŒ Ø§ØµÙ„ÛŒ Ú©Ø§ØªÙ„ÛŒÙ† Ø¨Ù‡ Ù‡Ù…Ø±Ø§Ù‡ Ù†Ø³Ø®Ù‡ JitPack Ù…Ù†ØªØ´Ø± Ù…ÛŒâ€ŒØ´ÙˆÙ†Ø¯! Ø¨Ø§ ÙØ´Ø±Ø¯Ù† Ø¯Ú©Ù…Ù‡ `Ctrl` Ùˆ Ú©Ù„ÛŒÚ© Ø±ÙˆÛŒ ØªÙˆØ§Ø¨Ø¹ Ø§ÛŒÙ† Ú©ØªØ§Ø¨Ø®Ø§Ù†Ù‡ Ø¯Ø± Ø§Ù†Ø¯Ø±ÙˆÛŒØ¯ Ø§Ø³ØªÙˆØ¯ÛŒÙˆØŒ Ø¨Ù‡ Ø¬Ø§ÛŒ ÙØ§ÛŒÙ„â€ŒÙ‡Ø§ÛŒ Ú©Ø§Ù…Ù¾Ø§ÛŒÙ„ Ø´Ø¯Ù‡ `.class`ØŒ Ø¯Ù‚ÛŒÙ‚Ø§Ù‹ Ø³ÙˆØ±Ø³ Ú©Ø¯Ù‡Ø§ÛŒ ÙˆØ§Ù‚Ø¹ÛŒ Ú©Ø§ØªÙ„ÛŒÙ† Ø±Ø§ Ù…Ø´Ø§Ù‡Ø¯Ù‡ Ø®ÙˆØ§Ù‡ÛŒØ¯ Ú©Ø±Ø¯.

---

## ðŸ“¦ Ø±Ø§Ù‡Ù†Ù…Ø§ÛŒ Ù…Ø§Ú˜ÙˆÙ„â€ŒÙ‡Ø§

Ø¯Ø± Ø§ÛŒÙ† Ø¨Ø®Ø´ Ø±Ø§Ù‡Ù†Ù…Ø§ÛŒ Ø¯Ù‚ÛŒÙ‚ Ùˆ Ù…Ø«Ø§Ù„â€ŒÙ‡Ø§ÛŒ Ú©Ø¯Ù‡Ø§ÛŒ ØªÙ…Ø§Ù…ÛŒ Û²Û° Ù…Ø§Ú˜ÙˆÙ„ Ù‚Ø±Ø§Ø± Ú¯Ø±ÙØªÙ‡ Ø§Ø³Øª. Ø¨Ø±Ø§ÛŒ Ù…Ø´Ø§Ù‡Ø¯Ù‡ Ø±ÙˆÛŒ Ù†Ø§Ù… Ù‡Ø± Ù…Ø§Ú˜ÙˆÙ„ Ú©Ù„ÛŒÚ© Ú©Ù†ÛŒØ¯!

<details>
<summary><b>1. ðŸ” easify-security (Ø±Ù…Ø²Ù†Ú¯Ø§Ø±ÛŒ Ùˆ Ù…Ø­Ø§ÙØ¸Øª Ø§Ø² ØµÙØ­Ù‡)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-security:2.0.0")`

Ø¬Ù„ÙˆÚ¯ÛŒØ±ÛŒ Ø§Ø² Ø§Ø³Ú©Ø±ÛŒÙ†â€ŒØ´Ø§Øª Ùˆ Ø¶Ø¨Ø· ØµÙØ­Ù‡ Ù†Ù…Ø§ÛŒØ´ØŒ Ø¨Ù‡ Ù‡Ù…Ø±Ø§Ù‡ Ø±Ù…Ø²Ù†Ú¯Ø§Ø±ÛŒ Ù¾ÛŒØ´Ø±ÙØªÙ‡ Ø³Ø®Øªâ€ŒØ§ÙØ²Ø§Ø±ÛŒ AES-GCM.

```kotlin
// Ø¬Ù„ÙˆÚ¯ÛŒØ±ÛŒ Ø§Ø² Ø§Ø³Ú©Ø±ÛŒÙ†â€ŒØ´Ø§Øª Ø¯Ø± Ø§Ú©ØªÛŒÙˆÛŒØªÛŒ/Ú©Ø§Ù…Ù¾ÙˆØ²Ø¨Ù„ ÙØ¹Ù„ÛŒ
val shield = rememberScreenShield()
shield.enable()

// Ø±Ù…Ø²Ù†Ú¯Ø§Ø±ÛŒ Ùˆ Ø±Ù…Ø²Ú¯Ø´Ø§ÛŒÛŒ Ø¢Ø³Ø§Ù† Ù…ØªÙ†
val encrypted = EasifyCrypto.encrypt("my_secret_text")
val decrypted = EasifyCrypto.decrypt(encrypted)
```
</details>

<details>
<summary><b>2. ðŸ›¡ï¸ easify-permission (Ø¯Ø³ØªØ±Ø³ÛŒâ€ŒÙ‡Ø§ Ø¯Ø± Ú©Ø§Ù…Ù¾ÙˆØ²)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-permission:2.0.0")`

Ù…Ø¯ÛŒØ±ÛŒØª Ú©Ø§Ù…Ù„Ø§Ù‹ Ø®ÙˆØ¯Ú©Ø§Ø± Ø¯Ø³ØªØ±Ø³ÛŒâ€ŒÙ‡Ø§ (Permissions) Ø¯Ø± Jetpack Compose Ø¨Ø¯ÙˆÙ† Ù†ÛŒØ§Ø² Ø¨Ù‡ Ú©Ø¯Ù‡Ø§ÛŒ ØªÚ©Ø±Ø§Ø±ÛŒ.

```kotlin
val permissionLauncher = rememberEasifyPermission(
    permission = Manifest.permission.CAMERA,
    onGranted = { /* Ø¯ÙˆØ±Ø¨ÛŒÙ† Ø¢Ù…Ø§Ø¯Ù‡ Ø§Ø³Øª */ },
    onDenied = { /* Ù†Ù…Ø§ÛŒØ´ Ø¯Ù„ÛŒÙ„ Ù†ÛŒØ§Ø² Ø¨Ù‡ Ø¯Ø³ØªØ±Ø³ÛŒ */ }
)

Button(onClick = { permissionLauncher.launch() }) {
    Text("Ø¯Ø±Ø®ÙˆØ§Ø³Øª Ø¯ÙˆØ±Ø¨ÛŒÙ†")
}
```
</details>

<details>
<summary><b>3. ðŸ‘† easify-biometric (Ø§Ø«Ø± Ø§Ù†Ú¯Ø´Øª Ùˆ ØªØ´Ø®ÛŒØµ Ú†Ù‡Ø±Ù‡)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-biometric:2.0.0")`

Ø±Ø§Ù‡â€ŒØ§Ù†Ø¯Ø§Ø²ÛŒ ÙÙˆÙ‚ Ø³Ø±ÛŒØ¹ Ø§Ø­Ø±Ø§Ø² Ù‡ÙˆÛŒØª Ø¨ÛŒÙˆÙ…ØªØ±ÛŒÚ© Ø¯Ø± Ø¨Ø±Ù†Ø§Ù…Ù‡.

```kotlin
val biometric = rememberBiometricLauncher(
    title = "ØªØ§ÛŒÛŒØ¯ Ù‡ÙˆÛŒØª",
    subtitle = "Ø¨Ø±Ø§ÛŒ Ø§Ø¯Ø§Ù…Ù‡ Ø§Ø² Ø§Ø«Ø± Ø§Ù†Ú¯Ø´Øª Ø§Ø³ØªÙØ§Ø¯Ù‡ Ú©Ù†ÛŒØ¯",
    onSuccess = { /* ÙˆØ±ÙˆØ¯ Ù…ÙˆÙÙ‚ */ },
    onError = { error -> /* Ù…Ø¯ÛŒØ±ÛŒØª Ø®Ø·Ø§ */ }
)

Button(onClick = { biometric.authenticate() }) {
    Text("ÙˆØ±ÙˆØ¯ Ø¨Ø§ Ø§Ø«Ø± Ø§Ù†Ú¯Ø´Øª")
}
```
</details>

<details>
<summary><b>4. ðŸŒ easify-network (Ù…Ø¯ÛŒØ±ÛŒØª Ktor Ùˆ Ø¢Ù¾Ù„ÙˆØ¯ ÙØ§ÛŒÙ„)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-network:2.0.0")`

Ù†Ø³Ø®Ù‡ Ø¨Ù‡ÛŒÙ†Ù‡â€ŒØ´Ø¯Ù‡ Ùˆ Ø§Ù…Ù† Ktor 3.x Ø¨Ø§ Ù…Ø¯ÛŒØ±ÛŒØª Ø®ÙˆØ¯Ú©Ø§Ø± ØªÙˆÚ©Ù†â€ŒÙ‡Ø§ Ùˆ Ø¢Ù¾Ù„ÙˆØ¯ Ø±Ø§Ø­Øª ÙØ§ÛŒÙ„â€ŒÙ‡Ø§ Ù‡Ù…Ø±Ø§Ù‡ Ø¨Ø§ Ù†Ù…Ø§ÛŒØ´ Ø¯Ø±ØµØ¯ Ù¾ÛŒØ´Ø±ÙØª.

```kotlin
// ÛŒÚ© Ø¯Ø±Ø®ÙˆØ§Ø³Øª GET Ø³Ø§Ø¯Ù‡
val result: EasifyResult<UserResponse> = EasifyNetwork.get("https://api.example.com/user")

// Ø¢Ù¾Ù„ÙˆØ¯ ÙØ§ÛŒÙ„ Ø¨Ø§ Ù†Ù…Ø§ÛŒØ´ Ø¯Ø±ØµØ¯ Ù¾ÛŒØ´Ø±ÙØª
EasifyNetwork.uploadMultipart(
    url = "https://api.example.com/upload",
    file = myFile,
    onProgress = { percent -> println("Ø¯Ø±ØµØ¯ Ø¢Ù¾Ù„ÙˆØ¯: $percent%") }
)
```
</details>

<details>
<summary><b>5. ðŸ‡®ðŸ‡· easify-persian (ØªØ§Ø±ÛŒØ® Ø´Ù…Ø³ÛŒ Ùˆ Ø§Ø¹Ø¯Ø§Ø¯ ÙØ§Ø±Ø³ÛŒ)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-persian:2.0.0")`

ØªØ¨Ø¯ÛŒÙ„ ØªÙ‚ÙˆÛŒÙ… Ù…ÛŒÙ„Ø§Ø¯ÛŒ Ø¨Ù‡ Ø´Ù…Ø³ÛŒØŒ ØªØ¨Ø¯ÛŒÙ„ Ø§Ø¹Ø¯Ø§Ø¯ Ø§Ù†Ú¯Ù„ÛŒØ³ÛŒ Ø¨Ù‡ ÙØ§Ø±Ø³ÛŒ Ùˆ Ø­Ø±ÙˆÙâ€ŒÙ†ÙˆÛŒØ³ÛŒ Ø§Ø¹Ø¯Ø§Ø¯.

```kotlin
// ØªØ¨Ø¯ÛŒÙ„ ØªØ§Ø±ÛŒØ® Ø¨Ù‡ Ø´Ù…Ø³ÛŒ
val jalali = Date().toJalaliString() // Ø®Ø±ÙˆØ¬ÛŒ: 1403/05/24

// ØªØ¨Ø¯ÛŒÙ„ Ø§Ø¹Ø¯Ø§Ø¯ Ø¨Ù‡ ÙØ±Ù…Øª ÙØ§Ø±Ø³ÛŒ
val text = "Ù‚ÛŒÙ…Øª: 1500".toPersianDigits() // Ø®Ø±ÙˆØ¬ÛŒ: Ù‚ÛŒÙ…Øª: Û±ÛµÛ°Û°

// ØªØ¨Ø¯ÛŒÙ„ Ø¹Ø¯Ø¯ Ø¨Ù‡ Ø­Ø±ÙˆÙ ÙØ§Ø±Ø³ÛŒ
val words = 1500000L.toPersianWords() // Ø®Ø±ÙˆØ¬ÛŒ: ÛŒÚ© Ù…ÛŒÙ„ÛŒÙˆÙ† Ùˆ Ù¾Ø§Ù†ØµØ¯ Ù‡Ø²Ø§Ø±
```
</details>

<details>
<summary><b>6. ðŸ’³ easify-fintech (Ø¨Ø§Ù†Ú©â€ŒÙ‡Ø§ Ùˆ Ø§Ø¹ØªØ¨Ø§Ø±Ø³Ù†Ø¬ÛŒ Ø§ÛŒØ±Ø§Ù†)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-fintech:2.0.0")`

Ø§Ø¹ØªØ¨Ø§Ø±Ø³Ù†Ø¬ÛŒ Ú©Ø¯ Ù…Ù„ÛŒØŒ Ø´Ù†Ø§Ø³Ø§ÛŒÛŒ Ù†Ø§Ù… Ø¨Ø§Ù†Ú© Ø§Ø² Ø±ÙˆÛŒ Ú©Ø§Ø±Øª Ø¨Ø§Ù†Ú©ÛŒ Ùˆ Ø§Ø¹ØªØ¨Ø§Ø±Ø³Ù†Ø¬ÛŒ Ø´Ù…Ø§Ø±Ù‡ Ø´Ø¨Ø§.

```kotlin
// Ø§Ø¹ØªØ¨Ø§Ø±Ø³Ù†Ø¬ÛŒ Ú©Ø¯ Ù…Ù„ÛŒ
val isValid = "0012345678".isValidIranianNationalId()

// Ø¯Ø±ÛŒØ§ÙØª Ù†Ø§Ù… Ø¨Ø§Ù†Ú© Ø§Ø² Ø±ÙˆÛŒ Ø´Ù…Ø§Ø±Ù‡ Ú©Ø§Ø±Øª
val bankName = BankCardUtils.getBankName("6037991234567890") // Ø®Ø±ÙˆØ¬ÛŒ: Bank Melli

// Ø§Ø¹ØªØ¨Ø§Ø±Ø³Ù†Ø¬ÛŒ Ø´Ù…Ø§Ø±Ù‡ Ø´Ø¨Ø§
val isValidSheba = "IR123...".isValidSheba()
```
</details>

<details>
<summary><b>7. ðŸ“³ easify-sensor (Ø³Ù†Ø³ÙˆØ±Ù‡Ø§ÛŒ Ø³Ø®Øªâ€ŒØ§ÙØ²Ø§Ø±ÛŒ)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-sensor:2.0.0")`

Ø¯Ø³ØªØ±Ø³ÛŒ ÙˆØ§Ú©Ù†Ø´â€ŒÚ¯Ø±Ø§ Ùˆ Ø³Ø§Ø¯Ù‡ Ø¨Ù‡ ØªÙ…Ø§Ù… Ø³Ù†Ø³ÙˆØ±Ù‡Ø§ÛŒ Ú¯ÙˆØ´ÛŒ (Ø´ØªØ§Ø¨â€ŒØ³Ù†Ø¬ØŒ Ù…Ø¬Ø§ÙˆØ±Øª Ùˆ ØºÛŒØ±Ù‡).

```kotlin
val accelerometer by rememberAccelerometerState()
Text("X: ${accelerometer.x}, Y: ${accelerometer.y}, Z: ${accelerometer.z}")

val isClose by rememberProximityState()
if (isClose) { Text("Ú¯ÙˆØ´ÛŒ Ù†Ø²Ø¯ÛŒÚ© Ú¯ÙˆØ´ Ø´Ù…Ø§Ø³Øª!") }
```
</details>

<details>
<summary><b>8. ðŸ”µ easify-bluetooth (Ø§Ø³Ú©Ù†Ø± Ø¨Ù„ÙˆØªÙˆØ«)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-bluetooth:2.0.0")`

Ø§Ø³Ú©Ù†Ø± Ù…Ø¯Ø±Ù† BLE Ú©Ù‡ Ù¾ÛŒÚ†ÛŒØ¯Ú¯ÛŒâ€ŒÙ‡Ø§ÛŒ Ø¯Ø³ØªØ±Ø³ÛŒ Ø¨Ù„ÙˆØªÙˆØ« Ø¯Ø± Ø§Ù†Ø¯Ø±ÙˆÛŒØ¯ Û±Û² Ø¨Ù‡ Ø¨Ø§Ù„Ø§ Ø±Ø§ Ù…Ø¯ÛŒØ±ÛŒØª Ù…ÛŒâ€ŒÚ©Ù†Ø¯.

```kotlin
val btScanner = rememberBluetoothScanner()
val devices by btScanner.devices.collectAsState(initial = emptyList())

Button(onClick = { btScanner.startScanning() }) {
    Text("Ø§Ø³Ú©Ù† Ø¨Ù„ÙˆØªÙˆØ«")
}
```
</details>

<details>
<summary><b>9. ðŸ“ easify-location (Ù…ÙˆÙ‚Ø¹ÛŒØªâ€ŒÛŒØ§Ø¨ÛŒ Ø²Ù†Ø¯Ù‡)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-location:2.0.0")`

Ø±Ø¯ÛŒØ§Ø¨ÛŒ Ù…ÙˆÙ‚Ø¹ÛŒØª Ù…Ú©Ø§Ù†ÛŒ Ø²Ù†Ø¯Ù‡ ØªÙ†Ù‡Ø§ Ø¨Ø§ ÛŒÚ© Ø®Ø· Ú©Ø¯ Ùˆ Ù…Ø¯ÛŒØ±ÛŒØª Ø®ÙˆØ¯Ú©Ø§Ø± Ø¯Ø³ØªØ±Ø³ÛŒâ€ŒÙ‡Ø§.

```kotlin
val locationState by rememberLocationTracker()

locationState?.let { loc ->
    Text("Lat: ${loc.latitude}, Lng: ${loc.longitude}")
}
```
</details>

<details>
<summary><b>10. ðŸŽ¨ easify-ui (Ø§ÙÚ©Øªâ€ŒÙ‡Ø§ Ùˆ Ø§Ø¨Ø²Ø§Ø±Ù‡Ø§ÛŒ Ø±Ø§Ø¨Ø· Ú©Ø§Ø±Ø¨Ø±ÛŒ)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-ui:2.0.0")`

Ø§Ø¨Ø²Ø§Ø±Ù‡Ø§ÛŒÛŒ Ù…Ø§Ù†Ù†Ø¯ Ø§ÙÚ©Øª Ú©Ù„ÛŒÚ© Ø¨Ù‡ Ø³Ø¨Ú© iOSØŒ Ø§ÙÚ©Øª Ø¨Ø§Ø±Ú¯Ø°Ø§Ø±ÛŒ Shimmer Ùˆ Ø¨Ø³ØªÙ† Ø®ÙˆØ¯Ú©Ø§Ø± Ú©ÛŒØ¨ÙˆØ±Ø¯.

```kotlin
// Ø§ÙÚ©Øª Ú©Ù„ÛŒÚ© Ø§Ø±ØªØ¬Ø§Ø¹ÛŒ (Bounce)
Card(modifier = Modifier.bounceClick { /* Ø§Ú©Ø´Ù† */ }) { ... }

// Ø§ÙÚ©Øª Shimmer (Ø¯Ø± Ø­Ø§Ù„ Ø¨Ø§Ø±Ú¯Ø°Ø§Ø±ÛŒ)
Box(modifier = Modifier.size(100.dp).shimmer(isLoading = true))

// Ø¨Ø³ØªÙ† Ú©ÛŒØ¨ÙˆØ±Ø¯ Ø¨Ø§ Ù„Ù…Ø³ Ø¨ÛŒØ±ÙˆÙ† Ú©Ø§Ø¯Ø±
Column(modifier = Modifier.hideKeyboardOnTapOutside()) { ... }
```
</details>

<details>
<summary><b>11. ðŸ“ easify-form (Ù…Ø¯ÛŒØ±ÛŒØª ÙØ±Ù…â€ŒÙ‡Ø§)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-form:2.0.0")`

Ù…Ø¯ÛŒØ±ÛŒØª ÙˆØ¶Ø¹ÛŒØª ÙØ±Ù…â€ŒÙ‡Ø§ Ø¯Ø± Ú©Ø§Ù…Ù¾ÙˆØ² Ø¨Ø§ Ù¾Ø´ØªÛŒØ¨Ø§Ù†ÛŒ Ø§Ø² Ø§Ø¹ØªØ¨Ø§Ø±Ø³Ù†Ø¬ÛŒ Ø®ÙˆØ¯Ú©Ø§Ø±.

```kotlin
val form = rememberEasifyForm()
val emailState = form.textField(name = "email", validators = listOf(EmailValidator()))

OutlinedTextField(
    value = emailState.value,
    onValueChange = emailState::onChange,
    isError = emailState.hasError
)

Button(onClick = { if(form.validate()) { /* Ø«Ø¨Øª Ø§Ø·Ù„Ø§Ø¹Ø§Øª */ } }) { Text("Ø§Ø±Ø³Ø§Ù„") }
```
</details>

<details>
<summary><b>12. ðŸ“³ easify-haptic (Ø¨Ø§Ø²Ø®ÙˆØ±Ø¯ Ù„Ø±Ø²Ø´ÛŒ Ù‡ÙˆØ´Ù…Ù†Ø¯)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-haptic:2.0.0")`

Ø§ÛŒØ¬Ø§Ø¯ Ø¨Ø§Ø²Ø®ÙˆØ±Ø¯Ù‡Ø§ÛŒ Ù„Ø±Ø²Ø´ÛŒ Ù…Ø¹Ù†Ø§Ø¯Ø§Ø± (Ù…ÙˆÙÙ‚ÛŒØªØŒ Ø®Ø·Ø§ØŒ Ú©Ù„ÛŒÚ©) Ø¨Ù‡ Ø³Ø§Ø¯Ú¯ÛŒ Ù‡Ø± Ú†Ù‡ ØªÙ…Ø§Ù…â€ŒØªØ±.

```kotlin
val haptic = LocalHapticFeedback.current

Button(onClick = { 
    // Ø§ÛŒØ¬Ø§Ø¯ Ù„Ø±Ø²Ø´ Ù…Ø®ØµÙˆØµ Ù¾ÛŒØ§Ù… Ù…ÙˆÙÙ‚ÛŒØª
    haptic.performEasifyHaptic(EasifyHapticType.SUCCESS) 
}) {
    Text("ØªÚ©Ù…ÛŒÙ„ ÙˆØ¸ÛŒÙÙ‡")
}
```
</details>

<details>
<summary><b>13. ðŸ”” easify-notification (Ù†ÙˆØªÛŒÙÛŒÚ©ÛŒØ´Ù†â€ŒÙ‡Ø§ÛŒ Ø³Ø§Ø¯Ù‡)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-notification:2.0.0")`

Ø³Ø§Ø®Øª Ùˆ Ù…Ø¯ÛŒØ±ÛŒØª Ø¢Ø³Ø§Ù† Ú©Ø§Ù†Ø§Ù„â€ŒÙ‡Ø§ÛŒ Ù†ÙˆØªÛŒÙÛŒÚ©ÛŒØ´Ù† Ùˆ Ù†Ù…Ø§ÛŒØ´ Ø§Ø¹Ù„Ø§Ù†â€ŒÙ‡Ø§.

```kotlin
EasifyNotification.show(
    context = context,
    title = "Ø¯Ø§Ù†Ù„ÙˆØ¯ Ú©Ø§Ù…Ù„ Ø´Ø¯",
    message = "ÙØ§ÛŒÙ„ Ø´Ù…Ø§ Ø¨Ø§ Ù…ÙˆÙÙ‚ÛŒØª Ø¯Ø±ÛŒØ§ÙØª Ø´Ø¯.",
    channelId = "downloads"
)
```
</details>

<details>
<summary><b>14. ðŸ“‚ easify-file (Ø§Ù†ØªØ®Ø§Ø¨Ú¯Ø± ÙØ§ÛŒÙ„ Ùˆ ØªØµÙˆÛŒØ±)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-file:2.0.0")`

Ø±Ø§Ø¨Ø· Ú©Ø§Ø±Ø¨Ø±ÛŒ Ø¨ØµØ±ÛŒ Ùˆ Ø³Ø§Ø¯Ù‡ Ø¨Ø±Ø§ÛŒ Ø§Ù†ØªØ®Ø§Ø¨ Ø¹Ú©Ø³ØŒ ÙˆÛŒØ¯ÛŒÙˆ ÛŒØ§ ÙØ§ÛŒÙ„.

```kotlin
val filePicker = rememberEasifyFilePicker(
    type = FileType.IMAGE,
    onResult = { uri -> /* Ø¹Ú©Ø³ Ø§Ù†ØªØ®Ø§Ø¨ Ø´Ø¯Ù‡ */ }
)

Button(onClick = { filePicker.launch() }) { Text("Ø§Ù†ØªØ®Ø§Ø¨ Ø¹Ú©Ø³") }
```
</details>

<details>
<summary><b>15. ðŸ—„ï¸ easify-storage (Ø°Ø®ÛŒØ±Ù‡â€ŒØ³Ø§Ø²ÛŒ Ø§Ù…Ù† DataStore)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-storage:2.0.0")`

ØªØ±Ú©ÛŒØ¨ÛŒ Ø§Ø² Preferences DataStore Ùˆ `easify-security` Ø¨Ø±Ø§ÛŒ Ø°Ø®ÛŒØ±Ù‡ Ú©Ø§Ù…Ù„Ø§Ù‹ Ø§Ù…Ù† Ùˆ Ø±Ù…Ø²Ù†Ú¯Ø§Ø±ÛŒâ€ŒØ´Ø¯Ù‡ Ø¯Ø§Ø¯Ù‡â€ŒÙ‡Ø§.

```kotlin
val storage = rememberEasifyStorage()

// Ø¯Ø§Ø¯Ù‡â€ŒÙ‡Ø§ Ø¨Ù‡ ØµÙˆØ±Øª Ø®ÙˆØ¯Ú©Ø§Ø± Ø±Ù…Ø²Ù†Ú¯Ø§Ø±ÛŒ Ù…ÛŒâ€ŒØ´ÙˆÙ†Ø¯
storage.saveSecure("auth_token", "jwt_abc123")

// Ù‡Ù†Ú¯Ø§Ù… Ø¯Ø±ÛŒØ§ÙØª Ø§Ø·Ù„Ø§Ø¹Ø§Øª Ø±Ù…Ø²Ú¯Ø´Ø§ÛŒÛŒ Ù…ÛŒâ€ŒØ´ÙˆÙ†Ø¯
val token = storage.getSecure("auth_token")
```
</details>

<details>
<summary><b>16. âš™ï¸ easify-context (Ø§Ø¨Ø²Ø§Ø±Ù‡Ø§ÛŒ Ø³ÛŒØ³ØªÙ… Ùˆ Ø§ÛŒÙ†ØªÙ†Øª)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-context:2.0.0")`

Ø§Ø¨Ø²Ø§Ø±Ù‡Ø§ÛŒÛŒ Ø¨Ø±Ø§ÛŒ Ú©Ù¾ÛŒ Ø¯Ø± Ú©Ù„ÛŒÙ¾â€ŒØ¨ÙˆØ±Ø¯ØŒ Ø§Ø´ØªØ±Ø§Ú©â€ŒÚ¯Ø°Ø§Ø±ÛŒ Ù…ØªÙ† Ùˆ Ø¨Ø§Ø² Ú©Ø±Ø¯Ù† ØªÙ†Ø¸ÛŒÙ…Ø§Øª Ø³ÛŒØ³ØªÙ….

```kotlin
context.copyToClipboard("Ù…ØªÙ† Ø¨Ø±Ø§ÛŒ Ú©Ù¾ÛŒ")
context.shareText("Ø§ÛŒÙ† Ø¨Ø±Ù†Ø§Ù…Ù‡ Ø±Ø§ Ø§Ù…ØªØ­Ø§Ù† Ú©Ù†!")
context.openAppSettings()
```
</details>

<details>
<summary><b>17. ðŸ”„ easify-lifecycle (Ú†Ø±Ø®Ù‡ Ø­ÛŒØ§Øª Ø¯Ø± Ú©Ø§Ù…Ù¾ÙˆØ²)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-lifecycle:2.0.0")`

Ù¾ÙˆØ´Ø´â€ŒÙ‡Ø§ÛŒ Ø¢Ø³Ø§Ù† Ø¨Ø±Ø§ÛŒ Ø§Ø³ØªÙØ§Ø¯Ù‡ Ø§Ø² LifeCycle Ø¯Ø±ÙˆÙ† Ú©Ø¯Ù‡Ø§ÛŒ Jetpack Compose.

```kotlin
useLifecycleEvent { event ->
    when(event) {
        Lifecycle.Event.ON_RESUME -> { /* Ø¨Ø±Ù†Ø§Ù…Ù‡ ÙØ¹Ø§Ù„ Ø´Ø¯ */ }
        Lifecycle.Event.ON_PAUSE -> { /* Ø¨Ø±Ù†Ø§Ù…Ù‡ Ø¯Ø± Ù¾Ø³â€ŒØ²Ù…ÛŒÙ†Ù‡ Ø±ÙØª */ }
        else -> {}
    }
}
```
</details>

<details>
<summary><b>18. âœ… easify-validation (Ø§Ø¹ØªØ¨Ø§Ø±Ø³Ù†Ø¬ÛŒ Ø¨Ø§ Regex)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-validation:2.0.0")`

Ù…Ø¬Ù…ÙˆØ¹Ù‡â€ŒØ§ÛŒ Ø§Ø² Ø§Ù„Ú¯ÙˆÙ‡Ø§ÛŒ Ø±Ø§ÛŒØ¬ Ø¨Ø±Ø§ÛŒ Ø§Ø¹ØªØ¨Ø§Ø±Ø³Ù†Ø¬ÛŒ Ø¹Ø¨Ø§Ø±Ø§Øª Ù…ØªÙ†ÛŒ.

```kotlin
val emailValid = "test@test.com".isEmailValid()
val phoneValid = "+989123456789".isPhoneNumberValid()
val passwordStrong = "Aa!123456".isStrongPassword()
```
</details>

<details>
<summary><b>19. ðŸ–¼ï¸ easify-image (Ù¾Ø±Ø¯Ø§Ø²Ø´ ØªØµØ§ÙˆÛŒØ±)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-image:2.0.0")`

ØªØºÛŒÛŒØ± Ø§Ù†Ø¯Ø§Ø²Ù‡ Ùˆ ÙØ´Ø±Ø¯Ù‡â€ŒØ³Ø§Ø²ÛŒ Ø¨Ø³ÛŒØ§Ø± Ø³Ø§Ø¯Ù‡ Ø¹Ú©Ø³â€ŒÙ‡Ø§ Ù‚Ø¨Ù„ Ø§Ø² Ø¢Ù¾Ù„ÙˆØ¯.

```kotlin
val optimizedBitmap = originalBitmap.compress(
    maxWidth = 1080,
    quality = 80
)
```
</details>

<details>
<summary><b>20. ðŸ”¢ easify-format (Ù‚Ø§Ù„Ø¨â€ŒØ¨Ù†Ø¯ÛŒ Ø±Ø´ØªÙ‡â€ŒÙ‡Ø§)</b></summary>

**Ú©Ø¯ Ù†ØµØ¨:** `implementation("com.github.ahbandegan.Easify-Android:easify-format:2.0.0")`

Ø¬Ø¯Ø§Ú©Ù†Ù†Ø¯Ù‡ Ø³Ù‡â€ŒØ±Ù‚Ù…ÛŒ Ù‚ÛŒÙ…Øªâ€ŒÙ‡Ø§ Ùˆ ØªØ¨Ø¯ÛŒÙ„ Ø²Ù…Ø§Ù† Ø¨Ù‡ ÙØ±Ù…Øª Ù‚Ø§Ø¨Ù„ Ø®ÙˆØ§Ù†Ø¯Ù†.

```kotlin
val price = 1500000.toCurrencyFormat() // Ø®Ø±ÙˆØ¬ÛŒ: "1,500,000"
val time = 3661000L.formatDuration()   // Ø®Ø±ÙˆØ¬ÛŒ: "01:01:01"
```
</details>

---

## ðŸ¤ Ù…Ø´Ø§Ø±Ú©Øª
Ù…Ø§ Ø§Ø² Ù…Ø´Ø§Ø±Ú©Øª Ø´Ù…Ø§ Ø§Ø³ØªÙ‚Ø¨Ø§Ù„ Ù…ÛŒâ€ŒÚ©Ù†ÛŒÙ…! Ø§Ú¯Ø± Ø§ÛŒØ¯Ù‡â€ŒØ§ÛŒ Ø¨Ø±Ø§ÛŒ Ù‚Ø§Ø¨Ù„ÛŒØªâ€ŒÙ‡Ø§ÛŒ Ø¬Ø¯ÛŒØ¯ "Easify" Ø¯Ø§Ø±ÛŒØ¯ØŒ Ù„Ø·ÙØ§Ù‹ Ø¨Ø§ Ø«Ø¨Øª Issue ÛŒØ§ Pull Request Ø¨Ù‡ Ù…Ø§ Ø§Ø·Ù„Ø§Ø¹ Ø¯Ù‡ÛŒØ¯.

## âš–ï¸ Ù„Ø§ÛŒØ³Ù†Ø³
Ù„Ø§ÛŒØ³Ù†Ø³ MIT. Ø§Ø³ØªÙØ§Ø¯Ù‡ Ø´Ø®ØµÛŒ Ùˆ ØªØ¬Ø§Ø±ÛŒ Ú©Ø§Ù…Ù„Ø§Ù‹ Ø±Ø§ÛŒÚ¯Ø§Ù† Ø§Ø³Øª.

