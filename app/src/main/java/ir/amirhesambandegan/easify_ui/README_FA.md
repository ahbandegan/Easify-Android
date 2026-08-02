# رابط کاربری در Easify

ماژول UI در Easify شامل ابزارهای Compose-friendly برای تعامل بهتر و کدنویسی تمیزتر است. این ماژول modifierهای سبک برای انیمیشن، مدیریت کیبورد، پشتیبانی از RTL و ابزارهای فضای‌دهی ارائه می‌دهد.

## امکانات اصلی
- `Modifier.bounceClick { ... }` برای بازخورد کلیک شبیه iOS
- `Modifier.hideKeyboardOnTapOutside()` برای بستن کیبورد با لمس خارج از فیلد
- `Modifier.shimmer(...)` برای placeholderهای لودینگ با افکت shimmer
- `Modifier.rtlMirror()` برای آینه‌سازی مناسب در RTL
- ابزارهای `SpacerWidth` و `SpacerHeight` برای فضای‌دهی ساده در layout

## مثال استفاده

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

## نکات مهم
- این ابزارها کد Compose شما را کوتاه‌تر و منظم‌تر می‌کنند.
- modifierها به‌راحتی با کامپوننت‌های موجود ترکیب می‌شوند.
