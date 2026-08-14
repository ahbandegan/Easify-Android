# easify_image

توابع کاربردی برای پردازش و تبدیل تصاویر (Bitmap) در اندروید.

## ویژگی‌ها
- **تبدیل URI به Bitmap**: رمزگشایی امن URI تصویر به یک شیء `Bitmap`.
- **فشرده‌سازی**: فشرده‌سازی آسان بیت‌مپ‌ها به `ByteArray` با کیفیت و فرمت قابل تنظیم.
- **تبدیل به Base64**: تبدیل بیت‌مپ‌ها به رشته‌های Base64 برای انتقال در شبکه.
- **تغییر اندازه**: تغییر مقیاس بیت‌مپ‌ها با حفظ نسبت ابعاد اصلی آن‌ها.

## نحوه استفاده

```kotlin
val bitmap = uri.toBitmap(context)

// تغییر اندازه به حداکثر ۱۰۲۴ در ۱۰۲۴
val resizedBitmap = bitmap?.resize(1024, 1024)

// تبدیل به رشته Base64
val base64String = resizedBitmap?.toBase64(quality = 80)

// دریافت بایت‌های فشرده خام
val bytes = resizedBitmap?.compressToBytes(format = Bitmap.CompressFormat.WEBP)
```
