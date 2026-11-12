package ir.amirhesambandegan.easify_fintech

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private val BANK_OTP_REGEX = Regex("(?<!\\d)\\d{5,7}(?!\\d)")

val ones = arrayOf(
    "", "یک", "دو", "سه", "چهار", "پنج", "شش", "هفت", "هشت", "نه",
    "ده", "یازده", "دوازده", "سیزده", "چهارده", "پانزده", "شانزده", "هفده", "هجده", "نوزده"
)
val tens = arrayOf("", "", "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود")
val hundreds = arrayOf("", "صد", "دویست", "سیصد", "چهارصد", "پانصد", "ششصد", "هفتصد", "هشتصد", "نهصد")
val scales = arrayOf("", "هزار", "میلیون", "میلیارد", "تریلیون", "کوآدریلیون", "کوینتیلیون")

/**
 * Extracts a 5, 6, or 7 digit OTP code from a Persian bank SMS.
 */
fun String.extractBankOtp(): String? = BANK_OTP_REGEX.find(toEnglishNumbers())?.value

/**
 * Helper object for working with Iranian IBAN (Sheba) numbers.
 */
object ShebaHelper {
    /**
     * Extracts the Persian bank name from an Iranian IBAN (`IR...`), returning `"نامشخص"` if invalid.
     */
    fun getBankNameFromSheba(sheba: String): String {
        val clean = sheba.toEnglishNumbers().uppercase().replace(" ", "").replace("-", "")
        if (!clean.startsWith("IR") || clean.length != 26) return "نامشخص"
        return when (clean.substring(4, 7)) {
            "010" -> "بانک مرکزی"
            "011" -> "بانک صنعت و معدن"
            "012" -> "بانک ملت"
            "013" -> "بانک رفاه"
            "014" -> "بانک مسکن"
            "015" -> "بانک سپه"
            "016" -> "بانک کشاورزی"
            "017" -> "بانک ملی"
            "018" -> "بانک تجارت"
            "019" -> "بانک صادرات"
            "020" -> "بانک توسعه صادرات"
            "021" -> "پست بانک"
            "022" -> "بانک توسعه تعاون"
            "051" -> "موسسه اعتباری توسعه"
            "052" -> "بانک قوامین"
            "053" -> "بانک کارآفرین"
            "054" -> "بانک پارسیان"
            "055" -> "بانک اقتصاد نوین"
            "056" -> "بانک سامان"
            "057" -> "بانک پاسارگاد"
            "058" -> "بانک سرمایه"
            "059" -> "بانک سینا"
            "060" -> "بانک قرض الحسنه مهر"
            "061" -> "بانک شهر"
            "062" -> "بانک آینده"
            "063" -> "بانک انصار"
            "064" -> "بانک گردشگری"
            "065" -> "بانک حکمت ایرانیان"
            "066" -> "بانک دی"
            "069" -> "بانک ایران زمین"
            "070" -> "بانک رسالت"
            "073" -> "موسسه اعتباری کوثر"
            "078" -> "بانک خاورمیانه"
            "093" -> "بانک مهر اقتصاد"
            else -> "نامشخص"
        }
    }
}

/**
 * Converts a [Long] number (including negatives and full 64-bit values) into Persian words.
 */
fun Long.toPersianWords(): String {
    if (this == 0L) return "صفر"
    if (this == Long.MIN_VALUE) {
        return "منفی نه کوینتیلیون و دویست و بیست و سه کوآدریلیون و سیصد و هفتاد و دو تریلیون و سی و شش میلیارد و هشتصد و پنجاه و چهار میلیون و هفتصد و هفتاد و پنج هزار و هشتصد و هشت"
    }
    if (this < 0L) return "منفی " + (-this).toPersianWords()

    var num = this
    var scaleIndex = 0
    val parts = mutableListOf<String>()
    while (num > 0L) {
        val chunk = (num % 1000L).toInt()
        if (chunk > 0) {
            val chunkStr = convertChunk(chunk)
            val scaleStr = scales.getOrElse(scaleIndex) { "" }
            parts.add(0, if (scaleStr.isNotEmpty()) "$chunkStr $scaleStr" else chunkStr)
        }
        num /= 1000L
        scaleIndex++
    }
    return parts.joinToString(" و ")
}

private fun convertChunk(number: Int): String {
    val parts = mutableListOf<String>()
    val h = number / 100
    val remainder = number % 100
    if (h > 0) parts.add(hundreds[h])
    if (remainder in 1..19) {
        parts.add(ones[remainder])
    } else if (remainder >= 20) {
        parts.add(tens[remainder / 10])
        val o = remainder % 10
        if (o > 0) parts.add(ones[o])
    }
    return parts.joinToString(" و ")
}

/**
 * Attempts to parse the string (normalizing Persian/Arabic digits) as a `Long` and convert it to Persian words.
 */
fun String.toPersianWords(): String = toEnglishNumbers().trim().replace(",", "").toLongOrNull()?.toPersianWords() ?: ""

/**
 * Lightweight Shamsi (Jalali) Date representation.
 */
data class ShamsiDate(var year: Int, var month: Int, var day: Int) {
    override fun toString(): String {
        val m = month.toString().padStart(2, '0')
        val d = day.toString().padStart(2, '0')
        return "$year/$m/$d"
    }
}

/**
 * Converts a Unix timestamp (in milliseconds) into a [ShamsiDate] without platform/JVM dependencies.
 */
@OptIn(ExperimentalTime::class)
fun Long.toShamsiDate(utcOffsetSeconds: Int = 3 * 3600 + 30 * 60): ShamsiDate {
    val adjustedMillis = this + utcOffsetSeconds * 1000L
    val daysSinceEpoch = adjustedMillis.floorDiv(86_400_000L)
    val z = daysSinceEpoch + 719468L
    val era = (if (z >= 0) z else z - 146096L) / 146097L
    val doe = (z - era * 146097L).toInt()
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
    val y = yoe + era.toInt() * 400
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp = (5 * doy + 2) / 153
    val gd = doy - (153 * mp + 2) / 5 + 1
    val gm = mp + (if (mp < 10) 3 else -9)
    val gy = y + (if (gm <= 2) 1 else 0)

    val gdm = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
    val gy2 = if (gm > 2) gy + 1 else gy
    var days = 355666 + (365 * gy) + ((gy2 + 3) / 4) - ((gy2 + 99) / 100) + ((gy2 + 399) / 400) + gd + gdm[gm - 1]
    var jy = -1595 + (33 * (days / 12053))
    days %= 12053
    jy += 4 * (days / 1461)
    days %= 1461
    if (days > 365) {
        jy += (days - 1) / 365
        days = (days - 1) % 365
    }
    val jm: Int
    val jd: Int
    if (days < 186) {
        jm = 1 + (days / 31)
        jd = 1 + (days % 31)
    } else {
        jm = 7 + ((days - 186) / 30)
        jd = 1 + ((days - 186) % 30)
    }
    return ShamsiDate(jy, jm, jd)
}
