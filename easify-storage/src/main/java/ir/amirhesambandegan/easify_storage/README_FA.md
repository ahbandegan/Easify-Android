# ذخیره‌سازی در Easify

ماژول ذخیره‌سازی در Easify یک wrapper ایمن و واکنشی برای Jetpack DataStore فراهم می‌کند. این ماژول به شما کمک می‌کند تنظیمات ساده را ذخیره کنید و همچنین مقادیر حساس را قبل از ذخیره رمزگذاری کنید.

## امکانات اصلی
- `PreferenceKeys` برای ساخت کلیدهای DataStore بدون کدنویسی تکراری
- `DataStoreController.update(...)` و `DataStoreController.get(...)` برای ذخیره و خواندن تنظیمات ساده
- `DataStoreController.saveSecure(...)` و `DataStoreController.getSecure(...)` برای مقادیر رمزگذاری‌شده
- `observe(...)` و `observeSecure(...)` برای دریافت به‌روزرسانی‌های ریاکتی

## مثال استفاده

```kotlin
import ir.amirhesambandegan.easify_storage.*

val controller = DataStoreController(context)
val tokenKey = PreferenceKeys.stringKey("token")

suspend fun saveToken() {
    controller.saveSecure(tokenKey, "secret-token")
}

suspend fun readToken() {
    val token = controller.getSecure(tokenKey)
    println(token)
}
```

## نکات مهم
- API امن از رمزگذاری مبتنی بر Android Keystore از طریق ماژول امنیت استفاده می‌کند.
- برای جلوگیری از تعریف مکرر کلیدها از `PreferenceKeys` استفاده کنید.
- این ماژول برای تنظیمات کاربر، فِلاگ‌ها و توکن‌ها مناسب است.
