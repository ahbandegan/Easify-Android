package ir.amirhesambandegan.easify_persian

private val NATIONAL_CODE_REGEX = Regex("^[0-9]{10}$")
private val PERSIAN_MOBILE_REGEX = Regex("^(?:0|98|\\+98|0098)?9[0-9]{9}$")
private val BANK_CARD_REGEX = Regex("^[0-9]{16}$")

/**
 * Validates Iranian National Code (کد ملی).
 * Normalizes Persian/Arabic digits, rejects all-identical 10-digit strings (e.g., `"0000000000"`, `"1111111111"`),
 * and checks the official Mod-11 checksum.
 */
fun String.isValidNationalCode(): Boolean {
    val clean = toEnglishDigits().trim()
    if (!NATIONAL_CODE_REGEX.matches(clean)) return false
    val first = clean[0]
    if (clean.all { it == first }) return false

    val check = clean[9] - '0'
    var sum = 0
    for (i in 0..8) {
        sum += (clean[i] - '0') * (10 - i)
    }
    val remainder = sum % 11
    return if (remainder < 2) check == remainder else check == 11 - remainder
}

/**
 * Validates Iranian Mobile Phone Number (شماره موبایل).
 * Accepts `"09..."`, `"+989..."`, `"00989..."`, `"989..."`, or `"9..."`, automatically normalizing Persian/Arabic digits.
 */
fun String.isValidPersianMobile(): Boolean {
    val clean = toEnglishDigits().replace(" ", "").replace("-", "").trim()
    return PERSIAN_MOBILE_REGEX.matches(clean)
}

/**
 * Validates a 16-digit Iranian bank card number using the Luhn checksum algorithm.
 * Ignores spaces and hyphens and normalizes Persian/Arabic digits.
 */
fun String.isValidBankCard(): Boolean {
    val clean = toEnglishDigits().replace(" ", "").replace("-", "").trim()
    if (!BANK_CARD_REGEX.matches(clean)) return false

    var sum = 0
    for (i in 0..15) {
        var digit = clean[i] - '0'
        if (i % 2 == 0) {
            digit *= 2
            if (digit > 9) digit -= 9
        }
        sum += digit
    }
    return sum % 10 == 0
}
