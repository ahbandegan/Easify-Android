package ir.amirhesambandegan.easify_format

import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToLong

private const val COMPACT_SUFFIXES = "KMGTPE"
private val FILE_SIZE_UNITS = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB")

/**
 * Formats a numeric value into a compact, human-readable string representation (e.g. `1.2K`, `3.5M`).
 * Supports negative values cleanly without throwing on [Long.MIN_VALUE].
 */
fun Long.toCompactFormat(): String {
    if (this in -999L..999L) return this.toString()
    val sign = if (this < 0L) "-" else ""
    val magnitude = abs(this.toDouble())
    val exp = (log10(magnitude) / log10(1000.0)).toInt().coerceIn(1, COMPACT_SUFFIXES.length)
    val scaled = magnitude / 1000.0.pow(exp.toDouble())
    val tenths = (scaled * 10.0).roundToLong()
    val whole = tenths / 10L
    val frac = (tenths % 10L).toInt()
    val numText = if (frac == 0) whole.toString() else "$whole.$frac"
    return "$sign$numText${COMPACT_SUFFIXES[exp - 1]}"
}

/**
 * Converts an integer into its ordinal string representation (e.g., `1st`, `2nd`, `3rd`).
 * Supports English (`"en"`) and Persian (`"fa"`) languages.
 */
fun Int.toOrdinal(language: String = "en"): String {
    if (language.lowercase() == "fa") {
        return when (this) {
            1 -> "اول"
            2 -> "دوم"
            3 -> "سوم"
            else -> "${this}م"
        }
    }

    val mod100 = abs(this % 100)
    if (mod100 in 11..13) return "${this}th"
    return when (abs(this % 10)) {
        1 -> "${this}st"
        2 -> "${this}nd"
        3 -> "${this}rd"
        else -> "${this}th"
    }
}

/**
 * Formats a byte count into a human-readable string (e.g., `1.50 MB`). Pure multiplatform implementation.
 */
fun Long.toReadableFileSize(): String {
    if (this <= 0L) return "0 B"
    val valueDouble = this.toDouble()
    val group = (log10(valueDouble) / log10(1024.0)).toInt().coerceIn(0, FILE_SIZE_UNITS.lastIndex)
    if (group == 0) return "$this B"
    val scaled = valueDouble / 1024.0.pow(group.toDouble())
    val hundredths = (scaled * 100.0).roundToLong()
    val whole = hundredths / 100L
    val frac = (hundredths % 100L).toString().padStart(2, '0')
    return "$whole.$frac ${FILE_SIZE_UNITS[group]}"
}

/**
 * Formats a [Number] as a deterministic grouping-separated currency string without JVM locale dependency.
 */
fun Number.toCurrencyString(
    symbol: String = "$",
    symbolPrefix: Boolean = true,
    decimals: Int = 2
): String {
    val safeDecimals = decimals.coerceIn(0, 6)
    val valueDouble = this.toDouble()
    val isNegative = valueDouble < 0.0
    val absVal = abs(valueDouble)

    val factor = 10.0.pow(safeDecimals)
    val scaledRounded = (absVal * factor).roundToLong()
    val factorLong = factor.roundToLong()
    val integerPart = if (safeDecimals == 0) scaledRounded else scaledRounded / factorLong
    val fractionPart = if (safeDecimals == 0) 0L else scaledRounded % factorLong

    val intFormatted = integerPart.toString().reversed().chunked(3).joinToString(",").reversed()
    val decimalPart = if (safeDecimals > 0) "." + fractionPart.toString().padStart(safeDecimals, '0') else ""
    val sign = if (isNegative && scaledRounded != 0L) "-" else ""
    val numberPart = "$intFormatted$decimalPart"

    return if (symbol.isEmpty()) {
        "$sign$numberPart"
    } else if (symbolPrefix) {
        "$sign$symbol$numberPart"
    } else {
        "$sign$numberPart $symbol"
    }
}
