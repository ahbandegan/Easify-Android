package ir.amirhesambandegan.easify_fintech

/**
 * Array of Persian words for numbers from 1 to 19.
 */
val ones = arrayOf("", "یک", "دو", "سه", "چهار", "پنج", "شش", "هفت", "هشت", "نه", "ده", "یازده", "دوازده", "سیزده", "چهارده", "پانزده", "شانزده", "هفده", "هجده", "نوزده")

/**
 * Array of Persian words for tens from 20 to 90.
 */
val tens = arrayOf("", "", "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود")

/**
 * Array of Persian words for hundreds from 100 to 900.
 */
val hundreds = arrayOf("", "صد", "دویست", "سیصد", "چهارصد", "پانصد", "ششصد", "هفتصد", "هشتصد", "نهصد")

/**
 * Array of Persian scale words (thousands, millions, etc.).
 */
val scales = arrayOf("", "هزار", "میلیون", "میلیارد", "تریلیون")

/**
 * Converts a `Long` number into its Persian word representation.
 *
 * @return The Persian text representing the number.
 */
fun Long.toPersianWords(): String {
    if (this == 0L) return "صفر"
    var num = this
    var scaleIndex = 0
    val parts = mutableListOf<String>()
    
    while (num > 0) {
        val chunk = (num % 1000).toInt()
        if (chunk > 0) {
            val chunkStr = convertChunk(chunk)
            val scaleStr = scales[scaleIndex]
            parts.add(0, if (scaleStr.isNotEmpty()) "$chunkStr $scaleStr" else chunkStr)
        }
        num /= 1000
        scaleIndex++
    }
    return parts.joinToString(" و ")
}

/**
 * Converts a 3-digit chunk of a number into Persian words.
 *
 * @param number The 3-digit number chunk to convert.
 * @return The Persian text representing the chunk.
 */
private fun convertChunk(number: Int): String {
    val parts = mutableListOf<String>()
    val h = number / 100
    val remainder = number % 100
    
    if (h > 0) parts.add(hundreds[h])
    
    if (remainder < 20 && remainder > 0) {
        parts.add(ones[remainder])
    } else if (remainder >= 20) {
        val t = remainder / 10
        val o = remainder % 10
        parts.add(tens[t])
        if (o > 0) parts.add(ones[o])
    }
    return parts.joinToString(" و ")
}

/**
 * Attempts to parse the string as a `Long` and converts it into its Persian word representation.
 *
 * @return The Persian text representing the number, or an empty string if parsing fails.
 */
fun String.toPersianWords(): String = this.toLongOrNull()?.toPersianWords() ?: ""
