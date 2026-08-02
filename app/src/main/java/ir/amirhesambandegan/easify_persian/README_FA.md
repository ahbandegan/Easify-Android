# فارسی در Easify

ماژول فارسی در Easify ابزارهایی برای قالب‌بندی و نمایش داده‌های فارسی و ایرانی ارائه می‌دهد. این ماژول به شما کمک می‌کند تاریخ، اعداد، مبالغ پول و زمان‌های نسبی را به شیوه‌ای ساده و خوانا تبدیل کنید.

## امکانات اصلی
- `Date.toJalali()` و `JalaliDate.toGregorian()` برای تبدیل بین تقویم میلادی و هجری شمسی
- `String.toPersianDigits()` برای تبدیل اعداد انگلیسی به فارسی
- `Number.toPersianCurrencyText()` برای ساخت متن پول به زبان فارسی
- `Long.toPersianWords()` برای تبدیل عدد به حروف فارسی
- `Long.toPersianTimeAgo()` برای ساخت متن زمان نسبی به فارسی

## مثال استفاده

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

## نکات مهم
- این ابزارها به‌ویژه برای برچسب‌های UI فارسی، نمایش مبلغ و قالب‌بندی تاریخ/ساعت بسیار کاربردی‌اند.
- توابع تبدیل سبک و ساده‌اند و به‌راحتی در ViewModel یا Compose استفاده می‌شوند.
