# Easify Security

Easify Security helps you protect sensitive data in Android apps with hardware-backed encryption. It uses Android Keystore and AES-GCM so that secrets can be stored and retrieved safely without storing plaintext in shared preferences or simple files.

## What it offers
- `EncryptionManager.encrypt(alias, text)` for secure encryption
- `EncryptionManager.decrypt(alias, encryptedText)` for safe decryption
- `ScreenShield()` composable to prevent screenshots and screen recording
- `Modifier.screenShield()` for attaching the protection to any composable

## Usage example

```kotlin
import ir.amirhesambandegan.easify_security.*

val token = "secret-token"
val encrypted = EncryptionManager.encrypt("user_token", token)
val decrypted = EncryptionManager.decrypt("user_token", encrypted)

println(decrypted)
```

```kotlin
@Composable
fun SecureScreen() {
    ScreenShield()

    Box(modifier = Modifier.fillMaxSize()) {
        Text("Protected content")
    }
}
```

## Notes
- The encryption key is stored in the Android Keystore, which provides stronger protection than plain shared preferences.
- Use a stable alias per data category, such as `auth_token` or `payment_secret`.
- `ScreenShield` is useful for payment, authentication, or private content screens.
