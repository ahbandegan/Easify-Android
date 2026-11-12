package ir.amirhesambandegan.easify_format

private fun normalizeToAsciiDigits(input: String): String {
    val sb = StringBuilder()
    for (ch in input) {
        when (ch) {
            in '0'..'9' -> sb.append(ch)
            in '\u06F0'..'\u06F9' -> sb.append(('0'.code + (ch.code - '\u06F0'.code)).toChar())
            in '\u0660'..'\u0669' -> sb.append(('0'.code + (ch.code - '\u0660'.code)).toChar())
        }
    }
    return sb.toString()
}

/**
 * Masks the email address by replacing parts of the local name with asterisks.
 * E.g., `"john.doe@example.com"` becomes `"joh***@example.com"`.
 */
fun String.maskEmail(): String {
    val parts = split("@")
    if (parts.size != 2) return this
    val name = parts[0]
    val domain = parts[1]
    if (name.isEmpty() || domain.isBlank()) return this

    val maskedName = if (name.length <= 3) {
        "${name.first()}***"
    } else {
        "${name.take(3)}***"
    }
    return "$maskedName@$domain"
}

/**
 * Masks a phone number by showing only the first four and last four digits.
 */
fun String.maskPhone(): String {
    val digits = normalizeToAsciiDigits(this)
    if (digits.length < 7) return this
    return "${digits.take(4)}***${digits.takeLast(4)}"
}

/**
 * Masks a 16-digit card number into `"1234 **** **** 5678"`.
 */
fun String.maskCard(): String {
    val digits = normalizeToAsciiDigits(this)
    if (digits.length != 16) return this
    return "${digits.take(4)} **** **** ${digits.takeLast(4)}"
}

/**
 * Masks the middle characters of an arbitrary sensitive string while keeping [prefixVisible] and [suffixVisible] chars.
 */
fun String.maskMiddle(
    prefixVisible: Int = 2,
    suffixVisible: Int = 2,
    maskChar: Char = '*'
): String {
    val safePrefix = prefixVisible.coerceAtLeast(0)
    val safeSuffix = suffixVisible.coerceAtLeast(0)
    if (length <= safePrefix + safeSuffix) return this
    val hiddenCount = length - safePrefix - safeSuffix
    return take(safePrefix) + maskChar.toString().repeat(hiddenCount) + takeLast(safeSuffix)
}
