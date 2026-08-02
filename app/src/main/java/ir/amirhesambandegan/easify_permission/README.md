# Easify Permission

Easify Permission simplifies Android runtime permission handling for Compose-based UIs. It gives you small composables that request single or multiple permissions and report the result through simple callbacks.

## What it offers
- `RequestPermission(...)` for a single runtime permission
- `RequestMultiplePermissions(...)` for a list of permissions
- `isGranted(permission, context)` to inspect the state of a permission
- `PermissionStats` enum for readable permission status values

## Usage example

```kotlin
import ir.amirhesambandegan.easify_permission.*

@Composable
fun CameraPermissionScreen() {
    var granted by remember { mutableStateOf(false) }

    RequestPermission(
        permission = Manifest.permission.CAMERA,
        onGrant = { granted = true },
        onDenied = { granted = false }
    )

    if (granted) {
        Text("Camera permission granted")
    }
}
```

## Notes
- The composables automatically check whether the permission is already granted before launching the request.
- Use `RequestMultiplePermissions` for cases like location, storage, or Bluetooth-related permission bundles.
- If you need more control, combine this module with your own UI state and dialogs.
