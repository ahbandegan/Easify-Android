# Easify Notification

Easify Notification provides a small utility for showing Android notifications from your app with minimal setup.

## What it offers
- `NotificationHelper.showNotification(...)` to display a simple notification

## Usage example

```kotlin
import ir.amirhesambandegan.easify_notification.*

NotificationHelper.showNotification(
    context = context,
    title = "Hello",
    message = "This is a notification from Easify"
)
```

## Notes
- Make sure the app has notification permission on supported Android versions.
- This helper is best suited for simple informational notifications.
