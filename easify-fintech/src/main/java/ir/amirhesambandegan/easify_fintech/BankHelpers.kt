package ir.amirhesambandegan.easify_fintech

/**
 * Extracts a 5, 6, or 7 digit OTP from a Persian bank SMS.
 */
fun String.extractBankOtp(): String? {
    val englishText = this.toEnglishNumbers()
    // Regex for exactly 5, 6, or 7 consecutive digits not surrounded by other digits
    val regex = Regex("(?<!\\d)\\d{5,7}(?!\\d)")
    return regex.find(englishText)?.value
}

object ShebaHelper {
    /**
     * Extracts the bank name from an Iranian IBAN (Sheba).
     */
    fun getBankNameFromSheba(sheba: String): String {
        if (!sheba.startsWith("IR", ignoreCase = true) || sheba.length != 26) return "نامشخص"
        
        val bankCode = sheba.substring(4, 7)
        return when (bankCode) {
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
