# easify_lifecycle

Jetpack Compose effects for responding to Android Lifecycle events seamlessly.

## Features
- **Generic Observer**: `LifecycleEventEffect` allows observing any specific `Lifecycle.Event`.
- **Convenience Effects**: Pre-defined Composables for common lifecycle events:
    - `OnCreateEffect`
    - `OnStartEffect`
    - `OnResumeEffect`
    - `OnPauseEffect`
    - `OnStopEffect`
    - `OnDestroyEffect`

## Usage

```kotlin
@Composable
fun MyScreen() {
    OnStartEffect {
        // Triggered when the lifecycle moves to ON_START
        println("Screen started")
    }

    OnResumeEffect {
        // Triggered when the lifecycle moves to ON_RESUME
        println("Screen resumed")
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        // Handle custom events
        println("Screen stopped")
    }
}
```
