package ir.amirhesambandegan.easify_fintech

import java.text.DecimalFormat

fun Long.toPriceFormat(currency: String = "تومان"): String {
    val formatter = DecimalFormat("#,###")
    return "${formatter.format(this)} $currency"
}

fun String.toPriceFormat(currency: String = "تومان"): String {
    return this.toLongOrNull()?.toPriceFormat(currency) ?: this
}

fun String.toEnglishNumbers(): String {
    var result = this
    val persianNumbers = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
    val arabicNumbers = arrayOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")
    for (i in 0..9) {
        result = result.replace(persianNumbers[i], i.toString())
        result = result.replace(arabicNumbers[i], i.toString())
    }
    return result
}

fun String.toPersianNumbers(): String {
    var result = this
    val persianNumbers = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
    for (i in 0..9) {
        result = result.replace(i.toString(), persianNumbers[i])
    }
    return result
}

fun Long.rialToToman(): Long = this / 10
fun Long.tomanToRial(): Long = this * 10

fun Long.withTax(percent: Int = 9): Long = this + (this * percent / 100)
fun Long.applyDiscount(percent: Int): Long = this - (this * percent / 100)
