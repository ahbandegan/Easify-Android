package ir.amirhesambandegan.easify_persian

import java.math.BigDecimal

/**
 * Converts a [Number] into a Persian currency string.
 * Example: 15000.toPersianCurrencyText("تومان") -> "پانزده هزار تومان"
 *
 * @param unit The currency unit (e.g., "تومان", "ریال"). Defaults to "تومان".
 * @return The formatted Persian currency string.
 */
fun Number.toPersianCurrencyText(unit: String = "تومان"): String {
    val longValue = when (this) {
        is BigDecimal -> this.toLong()
        else -> this.toLong()
    }
    return "${longValue.toPersianWords()} $unit"
}
