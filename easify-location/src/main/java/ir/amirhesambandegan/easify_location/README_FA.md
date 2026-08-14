# مکان‌یابی در Easify

ماژول مکان‌یابی در Easify APIهای Google Play Location را در یک تجربه ساده و سازگار با Compose بسته‌بندی می‌کند. این ماژول به شما کمک می‌کند بدون کدنویسی زیاد، به‌روزرسانی‌های موقعیت را دریافت کنید.

## امکانات اصلی
- `LocationTracker` برای دریافت به‌روزرسانی‌های موقعیت از طریق `Flow<Location?>`
- `rememberLocationTracker()` برای استفاده آسان در Compose
- درخواست خودکار مجوزهای دسترسی دقیق و کلی به موقعیت

## مثال استفاده

```kotlin
import ir.amirhesambandegan.easify_location.*

@Composable
fun LocationScreen() {
    val state = rememberLocationTracker()
    val tracker = state.tracker

    LaunchedEffect(Unit) {
        tracker.getLocationUpdates().collect { location ->
            location?.let { println(it.latitude to it.longitude) }
        }
    }
}
```

## نکات مهم
- این ماژول به APIهای Google Play Services Location وابسته است.
- مجوزها از طریق helper Compose به‌صورت خودکار درخواست می‌شوند.
- برای پیاده‌سازی ردیابی سبک موقعیت در اپ، مناسب است.
