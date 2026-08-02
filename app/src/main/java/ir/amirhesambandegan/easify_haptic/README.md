# easify_haptic

A collection of helpers for providing tactile haptic feedback.

## Features
- **Context Extensions**: Pre-defined vibration patterns for `hapticClick`, `hapticSuccess`, and `hapticError` using the `Vibrator` service.
- **Compose Extensions**: Semantic extensions for Jetpack Compose's `HapticFeedback` interface.

## Usage

### Using Context Extensions
```kotlin
context.hapticClick()
context.hapticSuccess()
context.hapticError()
```

### Using Compose Extensions
```kotlin
val haptic = LocalHapticFeedback.current

Button(onClick = {
    haptic.performClick()
    // Or
    haptic.performSuccess()
}) {
    Text("Tap with Feedback")
}
```
