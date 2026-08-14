package ir.amirhesambandegan.easify_format

import java.text.NumberFormat
import java.util.Locale

/**
 * Formats the numeric value as a currency string according to the specified [locale].
 *
 * @param locale The locale to use for formatting. Defaults to [Locale.getDefault].
 * @return A formatted currency string.
 */
fun Number.toCurrency(locale: Locale = Locale.getDefault()): String {
    return NumberFormat.getCurrencyInstance(locale).format(this)
}