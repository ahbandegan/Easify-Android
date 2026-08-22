package ir.amirhesambandegan.easify_format

import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

fun Long.toCompactFormat(): String {
    if (this < 1000) return this.toString()
    val exp = (log10(this.toDouble()) / log10(1000.0)).toInt()
    val format = DecimalFormat("0.#")
    val value = this / 1000.0.pow(exp.toDouble())
    return String.format("%s%c", format.format(value), "KMGTPE"[exp - 1])
}

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
