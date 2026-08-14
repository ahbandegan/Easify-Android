# easify_format

توابع الحاقی کاربردی برای قالب‌بندی رشته‌های ارز و زمان در اندروید.

## ویژگی‌ها
- **قالب‌بندی ارز**: تبدیل `Number` (مانند Int، Long، Double و غیره) به رشته‌های ارز مطابق با موقعیت مکانی (Locale).
- **قالب‌بندی زمان**: قالب‌بندی مدت زمان بر حسب میلی‌ثانیه (`Long`) به رشته‌های زمانی قابل خواندن برای انسان مانند `HH:mm:ss` یا `mm:ss`.

## نحوه استفاده

### قالب‌بندی ارز
```kotlin
val price = 1500000
val formatted = price.toCurrency() // خروجی متناسب با تنظیمات محلی ارز
```

### قالب‌بندی زمان
```kotlin
val durationMs = 3661000L // ۱ ساعت و ۱ دقیقه و ۱ ثانیه
val timeStr = durationMs.toTimeString() // "01:01:01"

val shortDurationMs = 75000L // ۷۵ ثانیه
val shortTimeStr = shortDurationMs.toTimeString() // "01:15"
```
