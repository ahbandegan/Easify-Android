# نوتیفیکیشن در Easify

ماژول نوتیفیکیشن در Easify یک ابزار ساده برای نمایش نوتیفیکیشن‌های اندرویدی با تنظیمات حداقلی فراهم می‌کند.

## امکانات اصلی
- `NotificationHelper.showNotification(...)` برای نمایش یک نوتیفیکیشن ساده

## مثال استفاده

```kotlin
import ir.amirhesambandegan.easify_notification.*

NotificationHelper.showNotification(
    context = context,
    title = "Hello",
    message = "This is a notification from Easify"
)
```

## نکات مهم
- در نسخه‌های پشتیبانی‌شده اندروید، اطمینان حاصل کنید اپ مجوز نوتیفیکیشن دارد.
- این ابزار برای نوتیفیکیشن‌های ساده و اطلاعاتی مناسب است.
