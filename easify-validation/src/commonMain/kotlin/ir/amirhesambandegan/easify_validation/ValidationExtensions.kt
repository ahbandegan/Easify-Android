package ir.amirhesambandegan.easify_validation

/**
 * Policy configuration for customizable password strength checks in [String.isValidPassword].
 */
data class PasswordPolicy(
    val minLength: Int = 8,
    val requireUppercase: Boolean = true,
    val requireLowercase: Boolean = true,
    val requireDigit: Boolean = true,
    val requireSpecialChar: Boolean = true,
    val allowWhitespace: Boolean = false
)

/**
 * Normalizes Persian (`۰-۹`) and Eastern Arabic (`٠-٩`) digits to ASCII digits (`0-9`).
 */
internal fun String.normalizeValidationDigits(): String {
    var hasNonAsciiDigit = false
    for (ch in this) {
        if (ch in '\u06F0'..'\u06F9' || ch in '\u0660'..'\u0669') {
            hasNonAsciiDigit = true
            break
        }
    }
    if (!hasNonAsciiDigit) return this

    val sb = StringBuilder(length)
    for (ch in this) {
        when (ch) {
            in '\u06F0'..'\u06F9' -> sb.append(('0'.code + (ch.code - 0x06F0)).toChar())
            in '\u0660'..'\u0669' -> sb.append(('0'.code + (ch.code - 0x0660)).toChar())
            else -> sb.append(ch)
        }
    }
    return sb.toString()
}

/**
 * Checks whether this string is a valid email address.
 */
fun String.isValidEmail(): Boolean = ValidationRegexes.emailRegex.matches(trim())

/**
 * Checks whether this string is a valid Iranian mobile number.
 * Automatically normalizes Persian/Arabic digits and ignores optional formatting spaces or hyphens.
 */
fun String.isValidMobile(allowInternational: Boolean = false): Boolean {
    val clean = normalizeValidationDigits().replace(" ", "").replace("-", "")
    val regex = if (allowInternational) {
        ValidationRegexes.mobileInternationalRegex
    } else {
        ValidationRegexes.mobileRegex
    }
    return regex.matches(clean)
}

/**
 * Validates an Iranian National ID (Code Melli) using both 10-digit format and the official Mod-11 checksum.
 * Rejects repeated identical digits such as `"0000000000"` or `"1111111111"`.
 */
fun String.isValidNationalId(): Boolean {
    val clean = normalizeValidationDigits().trim()
    if (!ValidationRegexes.nationalIdRegex.matches(clean)) return false
    val first = clean[0]
    if (clean.all { it == first }) return false

    val check = clean[9] - '0'
    var sum = 0
    for (i in 0 until 9) {
        sum += (clean[i] - '0') * (10 - i)
    }
    val remainder = sum % 11
    return if (remainder < 2) check == remainder else check == 11 - remainder
}

/**
 * Checks whether this string is a valid 10-digit Iranian postal code.
 * Set [strict] to `true` to enforce official Iranian postal prefix rules.
 */
fun String.isValidPostalCode(strict: Boolean = false): Boolean {
    val clean = normalizeValidationDigits().replace("-", "").trim()
    val regex = if (strict) {
        ValidationRegexes.postalCodeStrictRegex
    } else {
        ValidationRegexes.postalCodeRegex
    }
    return regex.matches(clean)
}

/**
 * Checks whether this string is a valid 11-digit Iranian landline phone number (`0xxxxxxxxxx`).
 */
fun String.isValidLandline(): Boolean {
    val clean = normalizeValidationDigits().replace(" ", "").replace("-", "")
    return ValidationRegexes.landlineRegex.matches(clean)
}

/**
 * Checks whether this string meets strong password requirements.
 */
fun String.isValidStrongPassword(): Boolean = ValidationRegexes.passwordStrongRegex.matches(this)

/**
 * Validates this string against a custom [PasswordPolicy].
 */
fun String.isValidPassword(policy: PasswordPolicy = PasswordPolicy()): Boolean {
    if (length < policy.minLength.coerceAtLeast(1)) return false
    if (!policy.allowWhitespace && any { it.isWhitespace() }) return false
    if (policy.requireUppercase && none { it in 'A'..'Z' }) return false
    if (policy.requireLowercase && none { it in 'a'..'z' }) return false
    if (policy.requireDigit && none { it in '0'..'9' }) return false
    if (policy.requireSpecialChar && none { !it.isLetterOrDigit() && !it.isWhitespace() }) return false
    return true
}

/**
 * Checks whether this string is a valid web URL.
 */
fun String.isValidUrl(): Boolean = ValidationRegexes.urlRegex.matches(trim())

/**
 * Checks whether this string is a valid username (3–20 alphanumeric characters or underscores).
 */
fun String.isValidUsername(): Boolean = ValidationRegexes.usernameRegex.matches(trim())

/**
 * Checks whether this string contains only Persian/Arabic script characters, spaces, and half-spaces (ZWNJ).
 */
fun String.isPersianText(): Boolean =
    isNotBlank() && ValidationRegexes.persianTextRegex.matches(this)

/**
 * Checks whether this string contains only English letters and spaces.
 */
fun String.isEnglishText(): Boolean =
    isNotBlank() && ValidationRegexes.englishTextRegex.matches(this)

/**
 * Checks whether this string is a valid Hex color (`#RGB`, `#RRGGBB`, or `#AARRGGBB`).
 */
fun String.isValidHexColor(): Boolean = ValidationRegexes.hexColorRegex.matches(trim())

/**
 * Checks whether this string is a valid IPv4 address (`0.0.0.0` to `255.255.255.255`).
 */
fun String.isValidIpv4(): Boolean = ValidationRegexes.ipv4Regex.matches(trim())

/**
 * Checks whether this string is a valid IPv6 address (full or zero-compressed `::` form, excluding zone IDs).
 */
fun String.isValidIpv6(): Boolean {
    val clean = trim()
    if (clean.isEmpty() || !clean.contains(':')) return false
    val doubleColonCount = clean.split("::").size - 1
    if (doubleColonCount > 1) return false

    val parts = clean.split(":")
    val hexHextet = Regex("^[0-9a-fA-F]{1,4}$")

    return if (doubleColonCount == 0) {
        parts.size == 8 && parts.all { hexHextet.matches(it) }
    } else {
        val halves = clean.split("::", limit = 2)
        val left = if (halves[0].isEmpty()) emptyList() else halves[0].split(":")
        val right = if (halves[1].isEmpty()) emptyList() else halves[1].split(":")
        val total = left.size + right.size
        total < 8 && left.all { hexHextet.matches(it) } && right.all { hexHextet.matches(it) }
    }
}

/**
 * Checks whether this string is a valid canonical UUID (`8-4-4-4-12`).
 */
fun String.isValidUuid(): Boolean = ValidationRegexes.uuidRegex.matches(trim())

/**
 * Checks whether this string is a valid lowercase URL slug (e.g. `"easify-android-v3"`).
 */
fun String.isValidSlug(): Boolean = ValidationRegexes.slugRegex.matches(trim())

/**
 * Checks whether this string represents a valid TCP/UDP port (`1..65535`, or `0..65535` if [allowZero] is `true`).
 */
fun String.isValidPort(allowZero: Boolean = false): Boolean {
    val port = normalizeValidationDigits().trim().toIntOrNull() ?: return false
    val minPort = if (allowZero) 0 else 1
    return port in minPort..65535
}

/**
 * Validates any 13–19 digit payment card number using the Luhn checksum algorithm.
 * Ignores spaces and hyphens, and automatically normalizes Persian/Arabic digits.
 */
fun String.isValidCreditCardLuhn(): Boolean {
    val digitsOnly = normalizeValidationDigits().replace(" ", "").replace("-", "")
    if (digitsOnly.length !in 13..19 || !digitsOnly.all { it in '0'..'9' }) return false

    var sum = 0
    var alternate = false
    for (i in digitsOnly.length - 1 downTo 0) {
        var n = digitsOnly[i] - '0'
        if (alternate) {
            n *= 2
            if (n > 9) n -= 9
        }
        sum += n
        alternate = !alternate
    }
    return sum % 10 == 0
}
