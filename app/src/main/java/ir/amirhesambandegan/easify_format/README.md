# easify_format

Utility extensions for formatting currency and time strings in Android.

## Features
- **Currency Formatting**: Convert `Number` (Int, Long, Double, etc.) to locale-aware currency strings.
- **Time Formatting**: Format millisecond durations (`Long`) into human-readable time strings like `HH:mm:ss` or `mm:ss`.

## Usage

### Currency Formatting
```kotlin
val price = 1500000
val formatted = price.toCurrency() // Results in locale-specific currency format
```

### Time Formatting
```kotlin
val durationMs = 3661000L // 1 hour, 1 minute, 1 second
val timeStr = durationMs.toTimeString() // "01:01:01"

val shortDurationMs = 75000L // 75 seconds
val shortTimeStr = shortDurationMs.toTimeString() // "01:15"
```
