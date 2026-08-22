package ir.amirhesambandegan.easify_persian

import java.math.BigDecimal

/**
 * Array of Persian names for units (0-9).
 */
private val units = arrayOf("", "یک", "دو", "سه", "چهار", "پنج", "شش", "هفت", "هشت", "نه")

/**
 * Array of Persian names for teens (10-19).
 */
private val teens = arrayOf("ده", "یازده", "دوازده", "سیزده", "چهارده", "پانزده", "شانزده", "هفده", "هجده", "نوزده")

/**
 * Array of Persian names for tens (20, 30, ..., 90).
 */
private val tens = arrayOf("", "", "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود")

/**
 * Array of Persian names for hundreds (100, 200, ..., 900).
 */
private val hundreds = arrayOf("", "یکصد", "دویست", "سیصد", "چهارصد", "پانصد", "ششصد", "هفتصد", "هشتصد", "نهصد")

/**
 * Array of Persian names for magnitude levels (thousands, millions, etc.).
 */
private val levels = arrayOf("", "هزار", "میلیون", "میلیارد", "تریلیارد")

/**
 * Converts a [Long] number into its Persian word representation.
 * Example: 1250 -> "یک هزار و دویست و پنجاه"
 *
 * @return The Persian word string representation of the number.
 */
fun Long.toPersianWords(): String {
    if (this == 0L) return "صفر"
    if (this < 0) return "منفی " + Math.abs(this).toPersianWords()

    val groups = mutableListOf<String>()
    var num = this
    var level = 0

    while (num > 0) {
        val threeDigits = (num % 1000).toInt()
        if (threeDigits != 0) {
            val groupText = convertThreeDigits(threeDigits)
            val levelText = if (levels[level].isEmpty()) groupText else "$groupText ${levels[level]}"
            groups.add(0, levelText)
        }
        num /= 1000
        level++
    }

    return groups.joinToString(" و ")
}

/**
 * Helper function to convert a three-digit integer into its Persian word representation.
 *
 * @param n The three-digit integer (0-999) to convert.
 * @return The Persian word string representation of the three digits.
 */
private fun convertThreeDigits(n: Int): String {
    val res = mutableListOf<String>()
    val h = n / 100
    val t = (n % 100) / 10
    val u = n % 10

    if (h > 0) res.add(hundreds[h])

    if (t == 1) {
        res.add(teens[u])
    } else {
        if (t > 1) res.add(tens[t])
        if (u > 0) res.add(units[u])
    }

    return res.joinToString(" و ")
}
