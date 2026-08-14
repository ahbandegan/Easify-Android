package ir.amirhesambandegan.easify_validation

/**
 * A collection of common Regular Expressions used for validation.
 */
object ValidationRegexes {

    /**
     * Regex for general email validation.
     */
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

    /**
     * Regex for Iranian Mobile Numbers (starts with 09 and 9 digits after).
     */
    val mobileRegex = Regex("^09\\d{9}\$")

    /**
     * Regex for Iranian National ID (10 digits).
     */
    val nationalIdRegex = Regex("^\\d{10}\$")

    /**
     * Regex for strong passwords:
     * - Minimum 8 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character
     */
    val passwordStrongRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")

    /**
     * Regex for Iranian 10-digit postal codes.
     */
    val postalCodeRegex = Regex("^\\d{10}\$")

    /**
     * Regex for Iranian landline phone numbers (e.g., 02188888888).
     */
    val landlineRegex = Regex("^0\\d{10}\$")

    /**
     * Regex for standard web URL validation.
     */
    val urlRegex = Regex("^(https?://)?(www\\.)?([\\w\\d-]+)\\.([\\w\\d]{2,})(/[\\w\\d-./?%&=]*)?\$")

    /**
     * Regex for usernames: 3-20 characters, alphanumeric and underscores only.
     */
    val usernameRegex = Regex("^[a-zA-Z0-9_]{3,20}\$")

    /**
     * Regex for validating that a string contains only Persian characters and spaces.
     */
    val persianTextRegex = Regex("^[\\u0600-\\u06FF\\s]+\$")

    /**
     * Regex for validating that a string contains only English characters and spaces.
     */
    val englishTextRegex = Regex("^[a-zA-Z\\s]+\$")
}
