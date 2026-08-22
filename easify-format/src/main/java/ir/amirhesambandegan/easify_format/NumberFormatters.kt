package ir.amirhesambandegan.easify_format

import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

/**
 * Formats a large number into a compact, human-readable string representation
 * (e.g., 1.2K, 3.5M).
 *
 * @return A compact string representation of the number. If the number is less than 1000,
 * it returns the number as a plain string.
 */
fun Long.toCompactFormat(): String {
    if (this < 1000) return this.toString()
    val exp = (log10(this.toDouble()) / log10(1000.0)).toInt()
    val format = DecimalFormat("0.#")
    val value = this / 1000.0.pow(exp.toDouble())
    return String.format("%s%c", format.format(value), "KMGTPE"[exp - 1])
}

/**
 * Converts an integer into its ordinal string representation (e.g., 1st, 2nd, 3rd).
 * Supports English ("en") and Persian ("fa") languages.
 *
 * @param language The language code to use for formatting. Defaults to "en".
 * @return The ordinal string representation of the integer in the specified language.
 */
fun Int.toOrdinal(language: String = "en"): String {
    if (language.lowercase() == "fa") {
        return when (this) {
            1 -> "اول"
            2 -> "دوم"
            3 -> "سوم"
            else -> this.toString() + "م"
        }
    }
    
    if (this % 100 in 11..13) return "${this}th"
    return when (this % 10) {
        1 -> "${this}st"
        2 -> "${this}nd"
        3 -> "${this}rd"
        else -> "${this}th"
    }
}
