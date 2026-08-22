package ir.amirhesambandegan.easify_format

/**
 * Masks the email address by replacing parts of the local name with asterisks.
 * E.g., "john.doe@example.com" becomes "joh***@example.com".
 *
 * @return The masked email address, or the original string if it is not a valid email format.
 */
fun String.maskEmail(): String {
    val parts = this.split("@")
    if (parts.size != 2) return this
    val name = parts[0]
    val domain = parts[1]
    
    val maskedName = if (name.length <= 3) {
        name.first() + "***"
    } else {
        name.take(3) + "***"
    }
    return "$maskedName@$domain"
}

/**
 * Masks a phone number by showing only the first four and last four digits,
 * replacing the middle digits with asterisks.
 *
 * @return The masked phone number, or the original string if it contains fewer than 7 digits.
 */
fun String.maskPhone(): String {
    val digits = this.filter { it.isDigit() }
    if (digits.length < 7) return this
    val start = digits.take(4)
    val end = digits.takeLast(4)
    return "$start***$end"
}

/**
 * Masks a 16-digit credit card number by showing only the first four and last four digits,
 * replacing the middle digits with asterisks. E.g., "1234 **** **** 5678".
 *
 * @return The masked credit card number, or the original string if it doesn't contain exactly 16 digits.
 */
fun String.maskCard(): String {
    val digits = this.filter { it.isDigit() }
    if (digits.length != 16) return this
    val start = digits.take(4)
    val end = digits.takeLast(4)
    return "$start **** **** $end"
}
