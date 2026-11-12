package ir.amirhesambandegan.easify_auth.sms

/**
 * Pure multiplatform utility for extracting numeric OTP codes from SMS messages.
 * Automatically normalizes Persian/Arabic-Indic digits before matching.
 */
object OtpExtractor {

    fun normalizeDigits(input: String): String {
        val sb = StringBuilder(input.length)
        for (char in input) {
            val mapped = when (char) {
                in '۰'..'۹' -> ('0'.code + (char - '۰')).toChar()
                in '٠'..'٩' -> ('0'.code + (char - '٠')).toChar()
                else -> char
            }
            sb.append(mapped)
        }
        return sb.toString()
    }

    fun extractOtp(message: String, lengthRange: IntRange = 4..8): String? {
        val normalized = normalizeDigits(message)
        val regex = Regex("\\b\\d{${lengthRange.first},${lengthRange.last}}\\b")
        return regex.find(normalized)?.value
    }
}
