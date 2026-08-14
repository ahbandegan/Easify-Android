# easify_haptic

مجموعه‌ای از توابع کمکی برای ارائه بازخورد لمسی (Haptic).

## ویژگی‌ها
- **توابع الحاقی Context**: الگوهای لرزش پیش‌تعریف شده برای `hapticClick` ، `hapticSuccess` و `hapticError` با استفاده از سرویس `Vibrator`.
- **توابع الحاقی Compose**: توابع الحاقی معنایی برای رابط `HapticFeedback` در Jetpack Compose.

## نحوه استفاده

### استفاده از توابع الحاقی Context
```kotlin
context.hapticClick()
context.hapticSuccess()
context.hapticError()
```

### استفاده از توابع الحاقی Compose
```kotlin
val haptic = LocalHapticFeedback.current

Button(onClick = {
    haptic.performClick()
    // یا
    haptic.performSuccess()
}) {
    Text("ضربه همراه با بازخورد")
}
```
