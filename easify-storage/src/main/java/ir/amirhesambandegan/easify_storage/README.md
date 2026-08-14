# Easify Storage

Easify Storage provides a secure and reactive wrapper around Jetpack DataStore. It helps you store simple preferences and also encrypt sensitive strings before saving them.

## What it offers
- `PreferenceKeys` helpers for creating DataStore keys without boilerplate
- `DataStoreController.update(...)` and `DataStoreController.get(...)` for basic preferences
- `DataStoreController.saveSecure(...)` and `DataStoreController.getSecure(...)` for encrypted values
- `observe(...)` and `observeSecure(...)` for reactive updates

## Usage example

```kotlin
import ir.amirhesambandegan.easify_storage.*

val controller = DataStoreController(context)
val tokenKey = PreferenceKeys.stringKey("token")

suspend fun saveToken() {
    controller.saveSecure(tokenKey, "secret-token")
}

suspend fun readToken() {
    val token = controller.getSecure(tokenKey)
    println(token)
}
```

## Notes
- The secure API uses Android Keystore-backed encryption through Easify Security.
- Use `PreferenceKeys` to avoid repeated key declarations in your app.
- This is a good fit for user settings, flags, and tokens.
