# اعتبارسنجی در Easify

ماژول اعتبارسنجی در Easify مجموعی از regexهای رایج را ارائه می‌دهد که می‌توانید در فرم‌ها، صفحه‌های ورود و جریان‌های اعتبارسنجی ورودی دوباره استفاده کنید.

## امکانات اصلی
- `ValidationRegexes.emailRegex`
- `ValidationRegexes.mobileRegex`
- `ValidationRegexes.nationalIdRegex`
- `ValidationRegexes.passwordStrongRegex`
- `ValidationRegexes.postalCodeRegex`
- `ValidationRegexes.landlineRegex`
- `ValidationRegexes.urlRegex`
- `ValidationRegexes.usernameRegex`
- `ValidationRegexes.persianTextRegex`
- `ValidationRegexes.englishTextRegex`

## مثال استفاده

```kotlin
import ir.amirhesambandegan.easify_validation.ValidationRegexes

val email = "user@example.com"
val isEmailValid = email.matches(ValidationRegexes.emailRegex)

val nationalId = "1234567890"
val isNationalIdValid = nationalId.matches(ValidationRegexes.nationalIdRegex)
```

## نکات مهم
- این regexها برای اعتبارسنجی سریع بدون نوشتن الگوهای تکراری بسیار مفیدند.
- آن‌ها را با منطق state فرم یا UI اعتبارسنجی خودتان ترکیب کنید.
