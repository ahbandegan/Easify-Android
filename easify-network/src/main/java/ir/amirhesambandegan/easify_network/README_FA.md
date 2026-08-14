# شبکه در Easify

ماژول شبکه در Easify یک لایه سبک و امن روی Ktor فراهم می‌کند. با این ماژول می‌توانید درخواست‌ها را ارسال کنید، خطاها را با `EasifyResult` مدیریت کنید، فایل‌ها را با فرمت چندبخشی آپلود کنید و وضعیت اتصال اینترنت را به‌صورت ریاکتی دنبال کنید.

## امکانات اصلی
- `createKtorClient(...)` برای ساخت `HttpClient` آماده و پیکربندی‌شده
- `HttpClient.safeRequest { ... }` برای اجرای درخواست‌ها با مدیریت خطای امن
- `HttpClient.uploadFile(...)` برای آپلود فایل با پشتیبانی از پیشرفت انتقال
- `ConnectivityObserver` برای نظارت بر وضعیت آنلاین/آفلاین با `Flow<Boolean>`
- `ConnectivityWrapper` برای نمایش UI جایگزین در Compose هنگام قطع اینترنت

## نصب
در فایل ساخت پروژه خود این وابستگی را اضافه کنید:

```kotlin
dependencies {
    implementation("ir.amirhesambandegan:easify-android:1.0.0")
}
```

## مثال استفاده

```kotlin
import io.ktor.client.HttpClient
import ir.amirhesambandegan.easify_network.*

val client = createKtorClient(
    baseUrl = "https://api.example.com",
    authToken = "demo-token"
)

suspend fun loadUser() {
    val result = client.safeRequest<Map<String, Any>> {
        get("/users/1")
    }

    when (result) {
        is EasifyResult.Success -> println(result.data)
        is EasifyResult.ApiError -> println(result.message)
        is EasifyResult.NetworkError -> println(result.exception.message)
        EasifyResult.Loading -> Unit
    }
}
```

## نکات مهم
- `safeRequest` استثناهای رایج شبکه را می‌گیرد و نتیجهٔ تایپ‌شده برمی‌گرداند.
- `uploadFile` یک `Uri` اندرویدی را می‌پذیرد و برای انتخاب فایل از پیکر یا دوربین مناسب است.
- `ConnectivityWrapper` برای صفحه‌های Compose که نیاز به UI جایگزین در حالت بدون اینترنت دارند، بسیار کاربردی است.
