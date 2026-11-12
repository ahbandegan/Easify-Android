package ir.amirhesambandegan.easify_fintech

import kotlin.math.abs

/**
 * Formats a `Long` value as a grouped price string appended with the specified currency unit.
 * Pure Kotlin Multiplatform implementation (supports negative values and [Long.MIN_VALUE]).
 */
fun Long.toPriceFormat(currency: String = "تومان"): String {
    val isNegative = this < 0L
    val digits = if (this == Long.MIN_VALUE) {
        "9223372036854775808"
    } else {
        abs(this).toString()
    }
    val grouped = digits.reversed().chunked(3).joinToString(",").reversed()
    val signed = if (isNegative) "-$grouped" else grouped
    return if (currency.isBlank()) signed else "$signed $currency"
}

/**
 * Attempts to parse the string (normalizing Persian/Arabic digits) as a `Long` and format it as a price string.
 */
fun String.toPriceFormat(currency: String = "تومان"): String {
    val normalized = trim().toEnglishNumbers().replace(",", "")
    return normalized.toLongOrNull()?.toPriceFormat(currency) ?: this
}

/**
 * Converts all Persian (`۰-۹`) and Arabic (`٠-٩`) digits in the string to ASCII English digits (`0-9`).
 */
fun String.toEnglishNumbers(): String {
    val sb = StringBuilder(length)
    for (ch in this) {
        when (ch) {
            in '\u06F0'..'\u06F9' -> sb.append(('0'.code + (ch.code - 0x06F0)).toChar())
            in '\u0660'..'\u0669' -> sb.append(('0'.code + (ch.code - 0x0660)).toChar())
            else -> sb.append(ch)
        }
    }
    return sb.toString()
}

/**
 * Converts all ASCII (`0-9`) and Eastern Arabic (`٠-٩`) digits in the string to Persian digits (`۰-۹`).
 */
fun String.toPersianNumbers(): String {
    val persian = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    val sb = StringBuilder(length)
    for (ch in this) {
        when (ch) {
            in '0'..'9' -> sb.append(persian[ch - '0'])
            in '\u0660'..'\u0669' -> sb.append(persian[ch.code - 0x0660])
            else -> sb.append(ch)
        }
    }
    return sb.toString()
}

/**
 * Converts a monetary value in Rial to Toman (divides by 10).
 */
fun Long.rialToToman(): Long = this / 10L

/**
 * Converts a monetary value in Toman to Rial (multiplies by 10).
 */
fun Long.tomanToRial(): Long = this * 10L

/**
 * Adds tax to a given amount based on the provided percentage (defaults to 9%).
 */
fun Long.withTax(percent: Int = 9): Long = this + (this * percent / 100L)

/**
 * Applies a discount to a given amount based on the provided percentage.
 */
fun Long.applyDiscount(percent: Int): Long = this - (this * percent / 100L)
