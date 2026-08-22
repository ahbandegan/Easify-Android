package ir.amirhesambandegan.easify_fintech

import java.text.DecimalFormat

/**
 * Formats a `Long` value as a price string with commas every three digits,
 * appended with the specified currency string.
 *
 * @param currency The currency string to append. Default is "تومان" (Toman).
 * @return The formatted price string.
 */
fun Long.toPriceFormat(currency: String = "تومان"): String {
    val formatter = DecimalFormat("#,###")
    return "${formatter.format(this)} $currency"
}

/**
 * Attempts to parse the string as a `Long` and format it as a price string.
 * If parsing fails, it returns the original string.
 *
 * @param currency The currency string to append. Default is "تومان" (Toman).
 * @return The formatted price string, or the original string if it is not a valid number.
 */
fun String.toPriceFormat(currency: String = "تومان"): String {
    return this.toLongOrNull()?.toPriceFormat(currency) ?: this
}

/**
 * Converts all Persian and Arabic digits in the string to English digits.
 *
 * @return A new string with English digits.
 */
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

/**
 * Converts all English digits in the string to Persian digits.
 *
 * @return A new string with Persian digits.
 */
fun String.toPersianNumbers(): String {
    var result = this
    val persianNumbers = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
    for (i in 0..9) {
        result = result.replace(i.toString(), persianNumbers[i])
    }
    return result
}

/**
 * Converts a monetary value in Rial to Toman (divides by 10).
 *
 * @return The equivalent value in Toman.
 */
fun Long.rialToToman(): Long = this / 10

/**
 * Converts a monetary value in Toman to Rial (multiplies by 10).
 *
 * @return The equivalent value in Rial.
 */
fun Long.tomanToRial(): Long = this * 10

/**
 * Adds tax to a given amount based on the provided percentage.
 *
 * @param percent The tax percentage to add. Default is 9%.
 * @return The total amount including tax.
 */
fun Long.withTax(percent: Int = 9): Long = this + (this * percent / 100)

/**
 * Applies a discount to a given amount based on the provided percentage.
 *
 * @param percent The discount percentage to subtract.
 * @return The final amount after the discount is applied.
 */
fun Long.applyDiscount(percent: Int): Long = this - (this * percent / 100)
