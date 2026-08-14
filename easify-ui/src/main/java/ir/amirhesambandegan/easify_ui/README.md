# Easify UI

Easify UI contains Compose-friendly helpers for smoother interactions and cleaner layouts. It includes lightweight modifiers for animations, keyboard handling, RTL support, and spacing utilities.

## What it offers
- `Modifier.bounceClick { ... }` for iOS-like tap feedback
- `Modifier.hideKeyboardOnTapOutside()` to dismiss the keyboard on outside taps
- `Modifier.shimmer(...)` for animated loading placeholders
- `Modifier.rtlMirror()` for RTL-friendly mirroring
- `SpacerWidth` and `SpacerHeight` helpers for simple layout spacing

## Usage example

```kotlin
import ir.amirhesambandegan.easify_ui.*

@Composable
fun DemoCard() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .bounceClick { println("Clicked") }
            .shimmer()
    ) {
        Text("Tap me")
    }
}
```

## Notes
- These utilities keep your Compose code concise and consistent.
- The modifiers are easy to combine with existing UI components.
