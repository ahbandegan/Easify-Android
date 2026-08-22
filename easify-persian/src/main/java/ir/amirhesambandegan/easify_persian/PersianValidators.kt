package ir.amirhesambandegan.easify_persian

/**
 * Validates Iranian National Code (کد ملی).
 */
fun String.isValidNationalCode(): Boolean {
    if (!matches(Regex("^[0-9]{10}$"))) return false
    val check = this[9].toString().toInt()
    val sum = (0..8).map { this[it].toString().toInt() * (10 - it) }.sum() % 11
    return if (sum < 2) check == sum else check + sum == 11
}

/**
 * Validates Iranian Mobile Phone Number (شماره موبایل).
 */
fun String.isValidPersianMobile(): Boolean {
    return matches(Regex("^(?:0|98|\\+98|0098)?9[0-9]{9}$"))
}

/**
 * Validates Iranian Bank Card Number (شماره کارت ۱۶ رقمی).
 */
fun String.isValidBankCard(): Boolean {
    if (!matches(Regex("^[0-9]{16}$"))) return false
    var sum = 0
    for (i in 0..15) {
        var digit = this[i].toString().toInt()
        if (i % 2 == 0) {
            digit *= 2
            if (digit > 9) digit -= 9
        }
        sum += digit
    }
    return sum % 10 == 0
}
