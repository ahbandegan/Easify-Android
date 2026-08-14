# ابزارهای فین‌تک (Easify Fintech)

مجموعه‌ای از ابزارهای اعتبارسنجی و فرمت‌دهی برای داده‌های مالی و شناسایی ایرانی.

## ویژگی‌های کلیدی

- **ابزارهای کارت بانکی**: اعتبارسنجی شماره کارت‌های ۱۶ رقمی (الگوریتم لوهان)، فرمت‌دهی برای نمایش و تشخیص نام بانک.
- **اعتبارسنجی شبا (IBAN)**: بررسی صحت شماره شبای ایران و شناسایی بانک صادرکننده.
- **کد ملی**: اعتبارسنجی شماره ملی ایران با استفاده از الگوریتم استاندارد چک‌سام.
- **تشخیص اپراتور موبایل**: شناسایی اپراتورهای موبایل ایرانی (همراه اول، ایرانسل، رایتل و غیره) از روی شماره تلفن.

## نحوه استفاده

### ۱. ابزارهای کارت بانکی

```kotlin
val cardNumber = "6037991234567890"

// اعتبارسنجی
if (cardNumber.isValidCardNumber()) {
    // فرمت‌دهی برای نمایش: "6037 9912 3456 7890"
    val formatted = cardNumber.formatCardNumber()
    
    // دریافت نام بانک: "Bank Melli"
    val bankName = cardNumber.getBankName()
}
```

### ۲. ابزارهای شماره شبا

```kotlin
val sheba = "IR120170000000123456789012"

if (sheba.isValidSheba()) {
    // دریافت نام بانک: "Bank Melli"
    val bankName = sheba.getShebaBankName()
}
```

### ۳. اعتبارسنجی کد ملی

```kotlin
val nationalId = "0012345678"

if (nationalId.isValidNationalId()) {
    // کد ملی معتبر است
}
```

### ۴. تشخیص اپراتور موبایل

```kotlin
val phone = "09121234567"
val operator = phone.getMobileOperator() // خروجی: "MCI"
```

## توابع الحاقی موجود

- `String.isValidCardNumber(): Boolean`
- `String.formatCardNumber(): String`
- `String.getBankName(): String?`
- `String.isValidSheba(): Boolean`
- `String.getShebaBankName(): String?`
- `String.isValidNationalId(): Boolean`
- `String.getMobileOperator(): String?`
