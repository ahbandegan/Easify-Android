# Easify Persian

Easify Persian provides utilities for Persian and Iranian-friendly formatting. It helps convert dates, numbers, currency values, and human-readable time expressions in a simple and readable way.

## What it offers
- `Date.toJalali()` and `JalaliDate.toGregorian()` for Jalali calendar conversion
- `String.toPersianDigits()` to convert Latin digits to Persian digits
- `Number.toPersianCurrencyText()` for Persian currency strings
- `Long.toPersianWords()` for converting numbers to Persian words
- `Long.toPersianTimeAgo()` for relative time text

## Usage example

```kotlin
import ir.amirhesambandegan.easify_persian.*
import java.util.Date

val now = Date()
val jalali = now.toJalali()
println(jalali) // 1403/05/14

val amount = 1500000.toPersianCurrencyText("تومان")
println(amount) // پانزده هزار و پانصد هزار تومان

val digits = "123456".toPersianDigits()
println(digits) // ۱۲۳۴۵۶
```

## Notes
- These helpers are especially useful for Persian UI labels, financial displays, and localized date/time rendering.
- The conversion methods are lightweight and work well inside Android ViewModel or Compose UI code.
