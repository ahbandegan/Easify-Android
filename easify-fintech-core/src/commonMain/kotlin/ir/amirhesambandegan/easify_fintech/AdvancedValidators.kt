package ir.amirhesambandegan.easify_fintech

/**
 * Represents the category of an Iranian utility/service bill decoded from its Bill ID.
 */
enum class BillType(val code: Int, val persianTitle: String) {
    WATER(1, "آب"),
    ELECTRICITY(2, "برق"),
    GAS(3, "گاز"),
    LANDLINE(4, "تلفن ثابت"),
    MOBILE(5, "تلفن همراه"),
    MUNICIPALITY(6, "عوارض شهرداری"),
    TAX(8, "سازمان امور مالیاتی"),
    TRAFFIC_FINE(9, "جریمه راهنمایی و رانندگی"),
    UNKNOWN(-1, "نامشخص");

    companion object {
        fun fromCode(code: Int): BillType = entries.firstOrNull { it.code == code } ?: UNKNOWN
    }
}

/**
 * Parsed metadata extracted from a valid Iranian Bill ID & Payment ID pair.
 */
data class BillInfo(
    val billId: String,
    val paymentId: String,
    val billType: BillType,
    val amountRials: Long,
    val periodCode: String
)

/**
 * Validates whether the given string is a valid 11-digit Iranian Legal Entity Identity (شناسه ملی اشخاص حقوقی).
 */
fun String.isValidLegalId(): Boolean {
    val id = toEnglishNumbers().trim()
    if (id.length != 11 || !id.all { it in '0'..'9' }) return false
    val checkDigit = id[10] - '0'
    val c = (id[9] - '0') + 2
    val coefficients = intArrayOf(29, 27, 23, 19, 17, 29, 27, 23, 19, 17)
    var sum = 0
    for (i in 0..9) {
        sum += ((id[i] - '0') + c) * coefficients[i]
    }
    val remainder = sum % 11
    val calculatedCheck = if (remainder == 10) 0 else remainder
    return checkDigit == calculatedCheck
}

/**
 * Validates whether the given string is a valid 10-digit Iranian postal code.
 */
fun String.isValidIranianPostalCode(): Boolean {
    val clean = toEnglishNumbers().replace("-", "").trim()
    if (clean.length != 10 || !clean.all { it in '0'..'9' }) return false
    return clean.matches(Regex("^(?!(\\d)\\1{3})[13-9]{4}[1346-9][013-9]{5}$"))
}

/**
 * Validates Iranian National ID (Code Melli) using the Mod-11 checksum algorithm.
 */
fun String.isValidNationalId(): Boolean {
    val clean = toEnglishNumbers().trim()
    if (clean.length != 10 || !clean.all { it in '0'..'9' }) return false
    if (clean.all { it == clean[0] }) return false

    val checkDigit = clean[9] - '0'
    var sum = 0
    for (i in 0 until 9) {
        sum += (clean[i] - '0') * (10 - i)
    }
    val remainder = sum % 11
    return if (remainder < 2) checkDigit == remainder else checkDigit == (11 - remainder)
}

/**
 * Detects the Iranian mobile operator based on the phone number prefix.
 */
fun String.getMobileOperator(): String? {
    var clean = toEnglishNumbers().filter { it in '0'..'9' }
    clean = when {
        clean.startsWith("0098") && clean.length > 4 -> "0" + clean.substring(4)
        clean.startsWith("98") && clean.length >= 12 -> "0" + clean.substring(2)
        clean.startsWith("9") && clean.length == 10 -> "0$clean"
        else -> clean
    }
    if (clean.length < 4) return null

    return when (clean.substring(0, 4)) {
        "0910", "0911", "0912", "0913", "0914", "0915", "0916", "0917", "0918", "0919",
        "0990", "0991", "0992", "0993", "0994", "0996" -> "MCI"

        "0930", "0933", "0935", "0936", "0937", "0938", "0939",
        "0900", "0901", "0902", "0903", "0904", "0905", "0941" -> "Irancell"

        "0920", "0921", "0922", "0923" -> "Rightel"
        "0998" -> "Shatel Mobile"
        "0999" -> "Samantel / Aptel"
        "0932" -> "Taliya"
        "0934" -> "TeleKish"
        else -> null
    }
}

/**
 * Validates and decodes Iranian utility bills (شناسه قبض و شناسه پرداخت) using full Base-11 checksum rules.
 */
object BillValidator {

    /**
     * Validates both bill ID and payment ID (including payment-only Base-11 check digit
     * and the combined Bill ID + Payment ID cross-check digit).
     */
    fun isValidBill(billId: String, paymentId: String): Boolean {
        val cleanBill = billId.toEnglishNumbers().trimStart('0').trim()
        val cleanPay = paymentId.toEnglishNumbers().trimStart('0').trim()
        if (cleanBill.length !in 6..13 || cleanPay.length !in 6..13) return false
        if (!cleanBill.all { it in '0'..'9' } || !cleanPay.all { it in '0'..'9' }) return false

        if (!checkBase11(cleanBill)) return false

        val payWithoutOuterCheck = cleanPay.dropLast(1)
        return checkBase11(payWithoutOuterCheck) && checkBase11(cleanBill + cleanPay)
    }

    /**
     * Parses [BillInfo] (amount in Rials, utility bill type, period) if [billId] and [paymentId] pass validation.
     */
    fun extractBillInfo(billId: String, paymentId: String): BillInfo? {
        val cleanBill = billId.toEnglishNumbers().trim()
        val cleanPay = paymentId.toEnglishNumbers().trim()
        if (!isValidBill(cleanBill, cleanPay)) return null

        val trimmedBill = cleanBill.trimStart('0')
        val trimmedPay = cleanPay.trimStart('0')
        val serviceTypeDigit = trimmedBill[trimmedBill.length - 2] - '0'
        val amountThousands = if (trimmedPay.length > 5) {
            trimmedPay.dropLast(5).toLongOrNull() ?: 0L
        } else {
            0L
        }
        val periodCode = trimmedPay.takeLast(5).take(3)

        return BillInfo(
            billId = trimmedBill,
            paymentId = trimmedPay,
            billType = BillType.fromCode(serviceTypeDigit),
            amountRials = amountThousands * 1000L,
            periodCode = periodCode
        )
    }

    internal fun checkBase11(code: String): Boolean {
        if (code.length < 2 || !code.all { it in '0'..'9' }) return false
        val checkDigit = code.last() - '0'
        val data = code.dropLast(1)
        var sum = 0
        var multiplier = 2
        for (i in data.length - 1 downTo 0) {
            sum += (data[i] - '0') * multiplier
            multiplier = if (multiplier == 7) 2 else multiplier + 1
        }
        val remainder = sum % 11
        val expected = if (remainder < 2) 0 else 11 - remainder
        return expected == checkDigit
    }
}
