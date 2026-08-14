# Easify Location

Easify Location wraps Google Play Location APIs in a simple and composable-friendly experience. It helps you request location updates with a minimal amount of boilerplate.

## What it offers
- `LocationTracker` for receiving location updates through a `Flow<Location?>`
- `rememberLocationTracker()` for Compose-friendly location handling
- automatic permission requests for fine/coarse location access

## Usage example

```kotlin
import ir.amirhesambandegan.easify_location.*

@Composable
fun LocationScreen() {
    val state = rememberLocationTracker()
    val tracker = state.tracker

    LaunchedEffect(Unit) {
        tracker.getLocationUpdates().collect { location ->
            location?.let { println(it.latitude to it.longitude) }
        }
    }
}
```

## Notes
- The module depends on Google Play Services Location APIs.
- Permissions are requested from the Compose helper automatically.
- Use it when you need a lightweight location tracking flow in your app.
