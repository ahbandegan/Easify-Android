package ir.amirhesambandegan.easify_fintech

val ones = arrayOf("", "یک", "دو", "سه", "چهار", "پنج", "شش", "هفت", "هشت", "نه", "ده", "یازده", "دوازده", "سیزده", "چهارده", "پانزده", "شانزده", "هفده", "هجده", "نوزده")
val tens = arrayOf("", "", "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود")
val hundreds = arrayOf("", "صد", "دویست", "سیصد", "چهارصد", "پانصد", "ششصد", "هفتصد", "هشتصد", "نهصد")
val scales = arrayOf("", "هزار", "میلیون", "میلیارد", "تریلیون")

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

fun String.toPersianWords(): String = this.toLongOrNull()?.toPersianWords() ?: ""
