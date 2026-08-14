# Easify Sensor

Easify Sensor makes hardware sensor access easier in Android apps. It offers reactive helpers for common sensors and simple data models that can be consumed in Compose or ViewModel code.

## What it offers
- `rememberSensorObserver(...)` to observe sensor events
- `SensorObserver` for listening to sensor updates
- `EasifySensorEvent` for structured sensor values
- `SensorUtils` for checking sensor availability

## Usage example

```kotlin
import ir.amirhesambandegan.easify_sensor.*

@Composable
fun SensorScreen() {
    val event = rememberSensorObserver(SensorType.ACCELEROMETER)

    LaunchedEffect(event) {
        event?.collect { value ->
            println(value.toThreeAxisData())
        }
    }
}
```

## Notes
- The module is useful for motion, proximity, and light-based interactions.
- Sensor availability can be checked before enabling a feature.
- The output values are exposed in easy-to-read data classes.
