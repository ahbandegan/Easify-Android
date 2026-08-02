# ماژول احراز هویت بیومتریک (Easify Biometric)

یک ماژول ساده و قدرتمند برای احراز هویت بیومتریک (اثر انگشت، تشخیص چهره و غیره) در اندروید که برای Jetpack Compose بهینه‌سازی شده است.

## ویژگی‌های کلیدی

- **بررسی سازگاری**: بررسی آسان اینکه آیا دستگاه از احراز هویت بیومتریک پشتیبانی می‌کند یا خیر.
- **دیالوگ بیومتریک**: یکپارچه‌سازی آسان با کتابخانه رسمی Biometric اندروید.
- **پشتیبانی از Compose**: دارای Composable اختصاصی `rememberBiometricLauncher` برای مدیریت آسان وضعیت.
- **مدیریت نتایج**: استفاده از کلاس `BiometricResult` برای مدیریت دقیق وضعیت‌های موفقیت، خطا و شکست.

## نحوه استفاده

### ۱. بررسی پشتیبانی از بیومتریک
از `BiometricUtils` برای بررسی قابلیت احراز هویت در دستگاه استفاده کنید.

```kotlin
val canAuth = BiometricUtils.canAuthenticate(context)
if (canAuth) {
    // نمایش دکمه ورود با اثر انگشت
}
```

### ۲. اجرای احراز هویت در Compose
با استفاده از `rememberBiometricLauncher` یک لانچر بسازید و نتایج را مدیریت کنید.

```kotlin
val biometricLauncher = rememberBiometricLauncher { result ->
    when (result) {
        is BiometricResult.Success -> {
            // احراز هویت با موفقیت انجام شد
        }
        is BiometricResult.Error -> {
            // خطایی رخ داد (مثلاً سخت‌افزار در دسترس نیست)
            val message = result.errString
        }
        is BiometricResult.Failed -> {
            // اثر انگشت شناسایی نشد یا کاربر لغو کرد
        }
    }
}

Button(onClick = {
    biometricLauncher?.launch(
        title = "ورود",
        subtitle = "برای ادامه احراز هویت کنید",
        description = "لطفاً انگشت خود را روی حسگر قرار دهید",
        negativeButtonText = "انصراف"
    )
}) {
    Text("تایید هویت")
}
```

## اجزا و کلاس‌ها

- `BiometricUtils`: ابزار کمکی برای بررسی قابلیت‌های دستگاه.
- `BiometricLauncher`: کلاس اصلی برای اجرای دیالوگ احراز هویت.
- `BiometricResult`: کلاس Sealed برای مدیریت وضعیت‌های `Success` (موفقیت)، `Error` (خطا) و `Failed` (شکست).
- `rememberBiometricLauncher`: تابع کمکی در Compose برای مدیریت طول عمر لانچر.
