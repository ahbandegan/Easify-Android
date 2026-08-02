# Easify Biometric

A simple and robust biometric authentication module for Android, designed for Jetpack Compose.

## Key Features

- **Compatibility Check**: Easily verify if the device supports biometric authentication.
- **Biometric Prompt**: Seamless integration with the official Android Biometric library.
- **Compose Support**: Built-in Composable `rememberBiometricLauncher` for easy state management.
- **Result Handling**: Clear result states using the `BiometricResult` sealed class.

## Usage

### 1. Check for Biometric Support
Use `BiometricUtils` to check if the device can authenticate using biometrics.

```kotlin
val canAuth = BiometricUtils.canAuthenticate(context)
if (canAuth) {
    // Show biometric button
}
```

### 2. Launch Biometric Prompt in Compose
Use `rememberBiometricLauncher` to obtain a launcher and handle the results.

```kotlin
val biometricLauncher = rememberBiometricLauncher { result ->
    when (result) {
        is BiometricResult.Success -> {
            // Authentication successful
            val authResult = result.result
        }
        is BiometricResult.Error -> {
            // An error occurred (e.g., hardware not available)
            val message = result.errString
        }
        is BiometricResult.Failed -> {
            // Recognition failed or user canceled
        }
    }
}

Button(onClick = {
    biometricLauncher?.launch(
        title = "Login",
        subtitle = "Authenticate to continue",
        description = "Please scan your fingerprint",
        negativeButtonText = "Cancel"
    )
}) {
    Text("Authenticate")
}
```

## Classes and Components

- `BiometricUtils`: Utility for capability checks.
- `BiometricLauncher`: Core class to trigger the authentication prompt.
- `BiometricResult`: Sealed class for `Success`, `Error`, and `Failed` states.
- `rememberBiometricLauncher`: Composable helper to manage the launcher lifecycle.
