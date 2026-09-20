package ir.amirhesambandegan.easify_auth.sms

/**
 * Utility for extracting numeric OTP codes from SMS text messages.
 * Automatically normalizes Persian/Arabic digits to English digits before extraction.
 */
object OtpExtractor {

    private val PERSIAN_ARABIC_DIGITS = mapOf(
        '۰' to '0', '۱' to '1', '۲' to '2', '۳' to '3', '۴' to '4',
        '۵' to '5', '۶' to '6', '۷' to '7', '۸' to '8', '۹' to '9',
        '٠' to '0', '١' to '1', '٢' to '2', '٣' to '3', '٤' to '4',
        '٥' to '5', '٦' to '6', '٧' to '7', '٨' to '8', '٩' to '9'
    )

    /**
     * Converts any Persian/Arabic digits in the string to standard ASCII digits.
     */
    fun normalizeDigits(input: String): String {
        val sb = StringBuilder(input.length)
        for (char in input) {
            sb.append(PERSIAN_ARABIC_DIGITS[char] ?: char)
        }
        return sb.toString()
    }

    /**
     * Extracts an OTP of length within [lengthRange] (default 4..8) from [message].
     *
     * @param message The SMS text message.
     * @param lengthRange Allowed digit length range for the OTP (e.g. 4..6 or 4..8).
     * @return Extracted OTP string, or null if no matching code was found.
     */
    fun extractOtp(message: String, lengthRange: IntRange = 4..8): String? {
        val normalized = normalizeDigits(message)
        val regex = Regex("\\b\\d{${lengthRange.first},${lengthRange.last}}\\b")
        val match = regex.find(normalized)
        return match?.value
    }
}
