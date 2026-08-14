# Easify Context

A collection of useful extension functions and utilities for Android `Context`.

## Key Features

- **Clipboard Management**: Easily copy to and get text from the system clipboard.
- **App Settings**: Quick access to the application's details settings page.
- **Intent Shortcuts**: Simplified methods for common tasks like calling, emailing, sharing, and opening maps.
- **Activity Finding**: Helper to find the parent `Activity` or `FragmentActivity` from any `Context`.

## Usage

### 1. Clipboard Operations

```kotlin
// Copy text
context.copyToClipboard("Hello World!", label = "MyText")

// Get text from clipboard
val text = context.getFromClipboard()
```

### 2. Opening App Settings

```kotlin
context.openAppSettings()
```

### 3. Common System Intents

```kotlin
// Make a phone call
context.makeCall("09123456789")

// Send an email
context.sendEmail(
    to = "example@mail.com",
    subject = "Hello",
    body = "This is a test email"
)

// Open a URL in browser
context.openBrowser("https://github.com")

// Share text
context.shareText("Check out this awesome library!")

// Open location on map
context.openMap(lat = 35.6892, lng = 51.3890, label = "Tehran")
```

### 4. Finding Activity from Context

```kotlin
val activity = context.findActivity()
val fragmentActivity = context.findFragmentActivity()
```

## Included Functions

- `copyToClipboard(text, label)`
- `getFromClipboard()`
- `openAppSettings()`
- `makeCall(phoneNumber)`
- `sendEmail(to, subject, body)`
- `openBrowser(url)`
- `shareText(text, title)`
- `openMap(lat, lng, label)`
- `findActivity()`
- `findFragmentActivity()`
