# مجوزها در Easify

ماژول مجوزها در Easify کار با مجوزهای زمان اجرا در اندروید را برای پروژه‌های Compose ساده‌تر می‌کند. این ماژول composableهای کوچک و کاربردی ارائه می‌دهد که مجوزهای تکی یا چندتایی را درخواست می‌کنند و نتیجه را از طریق callback ساده گزارش می‌دهند.

## امکانات اصلی
- `RequestPermission(...)` برای درخواست یک مجوز زمان اجرا
- `RequestMultiplePermissions(...)` برای درخواست چند مجوز به‌صورت همزمان
- `isGranted(permission, context)` برای بررسی وضعیت مجوز
- `PermissionStats` برای دسترسی به وضعیت مجوز به‌صورت خوانا

## مثال استفاده

```kotlin
import ir.amirhesambandegan.easify_permission.*

@Composable
fun CameraPermissionScreen() {
    var granted by remember { mutableStateOf(false) }

    RequestPermission(
        permission = Manifest.permission.CAMERA,
        onGrant = { granted = true },
        onDenied = { granted = false }
    )

    if (granted) {
        Text("Camera permission granted")
    }
}
```

## نکات مهم
- این composables قبل از درخواست، خودشان بررسی می‌کنند که آیا مجوز قبلاً داده شده است یا نه.
- برای مواردی مثل موقعیت مکانی، حافظه یا بلوتوث از `RequestMultiplePermissions` استفاده کنید.
- اگر کنترل بیشتری نیاز دارید، می‌توانید این ماژول را با state و دیالوگ‌های سفارشی خودتان ترکیب کنید.
