# Easify Network

Easify Network provides a lightweight and safe wrapper around Ktor for Android projects. It helps you make requests, handle errors with `EasifyResult`, upload files with multipart form data, and observe internet connectivity in a reactive way.

## What it offers
- `createKtorClient(...)` to build a ready-to-use `HttpClient`
- `HttpClient.safeRequest { ... }` to execute requests safely and map them to `EasifyResult`
- `HttpClient.uploadFile(...)` for multipart uploads with progress tracking
- `ConnectivityObserver` to watch online/offline state with `Flow<Boolean>`
- `ConnectivityWrapper` to show a no-connection overlay in Compose

## Installation
Add the dependency for your module setup in your app build file.

```kotlin
dependencies {
    implementation("ir.amirhesambandegan:easify-android:1.0.0")
}
```

## Usage example

```kotlin
import io.ktor.client.HttpClient
import ir.amirhesambandegan.easify_network.*

val client = createKtorClient(
    baseUrl = "https://api.example.com",
    authToken = "demo-token"
)

suspend fun loadUser() {
    val result = client.safeRequest<Map<String, Any>> {
        get("/users/1")
    }

    when (result) {
        is EasifyResult.Success -> println(result.data)
        is EasifyResult.ApiError -> println(result.message)
        is EasifyResult.NetworkError -> println(result.exception.message)
        EasifyResult.Loading -> Unit
    }
}
```

## Notes
- `safeRequest` catches common request exceptions and returns a typed result.
- `uploadFile` accepts an Android `Uri` and can be used from a picker or camera result.
- `ConnectivityWrapper` is especially useful for Compose screens that need a network-state fallback UI.
