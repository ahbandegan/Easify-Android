# سنسورها در Easify

ماژول سنسورها در Easify دسترسی به سنسورهای سخت‌افزاری اندروید را ساده‌تر می‌کند. این ماژول ابزارهای واکنشی برای سنسورهای رایج و مدل‌های داده‌ای ساده ارائه می‌دهد که در Compose یا ViewModel به‌راحتی استفاده می‌شوند.

## امکانات اصلی
- `rememberSensorObserver(...)` برای دریافت رویدادهای سنسور
- `SensorObserver` برای گوش‌دادن به به‌روزرسانی‌های سنسور
- `EasifySensorEvent` برای داده‌های ساختارمند سنسور
- `SensorUtils` برای بررسی در دسترس بودن سنسور

## مثال استفاده

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

## نکات مهم
- این ماژول برای تعامل‌های مبتنی بر حرکت، مجاورت و نور کاربردی است.
- قبل از فعال‌سازی یک قابلیت، می‌توان در دسترس بودن سنسور را بررسی کرد.
- خروجی‌ها در قالب data class‌های ساده و قابل خواندن ارائه می‌شوند.
