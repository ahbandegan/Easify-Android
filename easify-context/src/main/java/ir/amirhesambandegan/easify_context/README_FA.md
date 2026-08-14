# ابزارهای کانتکست (Easify Context)

مجموعه‌ای از توابع الحاقی (Extension Functions) و ابزارهای کاربردی برای `Context` در اندروید.

## ویژگی‌های کلیدی

- **مدیریت حافظه موقت (Clipboard)**: کپی کردن متن و دریافت متن از کلیپ‌بورد سیستم به سادگی.
- **تنظیمات اپلیکیشن**: دسترسی سریع به صفحه تنظیمات (Details Settings) برنامه.
- **میانبرهای Intent**: متدهای ساده‌سازی شده برای کارهای رایج مانند تماس، ایمیل، اشتراک‌گذاری و باز کردن نقشه.
- **یافتن Activity**: ابزار کمکی برای پیدا کردن `Activity` یا `FragmentActivity` والد از طریق هر `Context`.

## نحوه استفاده

### ۱. عملیات کلیپ‌بورد

```kotlin
// کپی کردن متن
context.copyToClipboard("سلام دنیا!", label = "MyText")

// دریافت متن از کلیپ‌بورد
val text = context.getFromClipboard()
```

### ۲. باز کردن تنظیمات برنامه

```kotlin
context.openAppSettings()
```

### ۳. اینتنت‌های رایج سیستمی

```kotlin
// تماس تلفنی
context.makeCall("09123456789")

// ارسال ایمیل
context.sendEmail(
    to = "example@mail.com",
    subject = "سلام",
    body = "این یک ایمیل تست است"
)

// باز کردن لینک در مرورگر
context.openBrowser("https://github.com")

// اشتراک‌گذاری متن
context.shareText("این کتابخانه فوق‌العاده را ببینید!")

// باز کردن موقعیت روی نقشه
context.openMap(lat = 35.6892, lng = 51.3890, label = "تهران")
```

### ۴. پیدا کردن اکتیویتی از کانتکست

```kotlin
val activity = context.findActivity()
val fragmentActivity = context.findFragmentActivity()
```

## توابع موجود

- `copyToClipboard(text, label)`
- `getFromClipboard()`
- `openAppSettings()`
- `makeCall(phoneNumber)`
- `sendEmail(to, subject, body)`
- `openBrowser(url)`
- `shareText(text, title)`
- `openMap(lat, lng, label)`
- `findActivity()`
- `findFragmentActivity()`
