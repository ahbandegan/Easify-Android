# امنیت در Easify

ماژول امنیت در Easify به شما کمک می‌کند داده‌های حساس را در اپ اندروید با رمزگذاری مبتنی بر سخت‌افزار محافظت کنید. این ماژول از Android Keystore و AES-GCM استفاده می‌کند تا رازها بدون ذخیره متن خام در SharedPreferences یا فایل‌های ساده، ایمن ذخیره و بازیابی شوند.

## امکانات اصلی
- `EncryptionManager.encrypt(alias, text)` برای رمزگذاری امن
- `EncryptionManager.decrypt(alias, encryptedText)` برای رمزگشایی ایمن
- `ScreenShield()` برای جلوگیری از گرفتن اسکرین‌شات و ضبط صفحه
- `Modifier.screenShield()` برای اعمال حفاظت روی هر composable

## مثال استفاده

```kotlin
import ir.amirhesambandegan.easify_security.*

val token = "secret-token"
val encrypted = EncryptionManager.encrypt("user_token", token)
val decrypted = EncryptionManager.decrypt("user_token", encrypted)

println(decrypted)
```

```kotlin
@Composable
fun SecureScreen() {
    ScreenShield()

    Box(modifier = Modifier.fillMaxSize()) {
        Text("Protected content")
    }
}
```

## نکات مهم
- کلید رمزنگاری در Android Keystore نگهداری می‌شود و نسبت به SharedPreferences ساده امنیت بالاتری دارد.
- برای هر دسته داده از alias ثابت و معنادار استفاده کنید؛ مثلاً `auth_token` یا `payment_secret`.
- `ScreenShield` برای صفحه‌های پرداخت، احراز هویت یا محتوای خصوصی بسیار مفید است.
