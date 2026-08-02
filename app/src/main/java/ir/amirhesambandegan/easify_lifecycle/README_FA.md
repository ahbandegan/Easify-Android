# easify_lifecycle

افکت‌های Jetpack Compose برای پاسخگویی یکپارچه به رویدادهای Lifecycle اندروید.

## ویژگی‌ها
- **مشاهده‌گر عمومی**: تابع `LifecycleEventEffect` اجازه مشاهده هر `Lifecycle.Event` خاصی را می‌دهد.
- **افکت‌های آماده**: Composableهای از پیش تعریف شده برای رویدادهای رایج لایف‌سایکل:
    - `OnCreateEffect`
    - `OnStartEffect`
    - `OnResumeEffect`
    - `OnPauseEffect`
    - `OnStopEffect`
    - `OnDestroyEffect`

## نحوه استفاده

```kotlin
@Composable
fun MyScreen() {
    OnStartEffect {
        // زمانی که لایف‌سایکل به ON_START تغییر می‌کند اجرا می‌شود
        println("صفحه شروع شد")
    }

    OnResumeEffect {
        // زمانی که لایف‌سایکل به ON_RESUME تغییر می‌کند اجرا می‌شود
        println("صفحه از سر گرفته شد")
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        // مدیریت رویدادهای سفارشی
        println("صفحه متوقف شد")
    }
}
```
