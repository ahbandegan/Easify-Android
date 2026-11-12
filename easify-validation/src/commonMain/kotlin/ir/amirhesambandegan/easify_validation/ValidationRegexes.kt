package ir.amirhesambandegan.easify_validation

/**
 * A multiplatform singleton object containing common Regular Expressions used for data validation
 * across Console (JVM/Native), Web (JS/Wasm), Desktop, iOS, and Android applications.
 */
object ValidationRegexes {

    /**
     * Regular expression for validating standard email addresses.
     */
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    /**
     * Regular expression for validating standard Iranian mobile numbers (`09xxxxxxxxx`).
     */
    val mobileRegex = Regex("^09\\d{9}$")

    /**
     * Flexible regular expression for validating Iranian mobile numbers in domestic or
     * international formats (`09xxxxxxxxx`, `+989xxxxxxxxx`, `00989xxxxxxxxx`, `9xxxxxxxxx`).
     */
    val mobileInternationalRegex = Regex("^(?:\\+98|0098|0)?9\\d{9}$")

    /**
     * Regular expression for validating the 10-digit shape of Iranian National IDs (Code Melli).
     * Use [String.isValidNationalId] to also verify the official Mod-11 checksum.
     */
    val nationalIdRegex = Regex("^\\d{10}$")

    /**
     * Regular expression for validating strong passwords:
     * - Minimum 8 characters
     * - At least one lowercase letter (`a-z`), one uppercase letter (`A-Z`),
     *   one digit (`0-9`), and one special character (`@$!%*?&#^+=._-`).
     */
    val passwordStrongRegex = Regex(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^+=._-])[A-Za-z\\d@$!%*?&#^+=._-]{8,}$"
    )

    /**
     * Regular expression for validating 10-digit Iranian postal codes.
     */
    val postalCodeRegex = Regex("^\\d{10}$")

    /**
     * Strict regular expression for Iranian postal codes following official digit rules.
     */
    val postalCodeStrictRegex = Regex("^(?!(\\d)\\1{3})[13-9]{4}[1346-9][013-9]{5}$")

    /**
     * Regular expression for validating Iranian landline numbers with area code (`0xxxxxxxxxx`).
     */
    val landlineRegex = Regex("^0\\d{10}$")

    /**
     * Regular expression for validating HTTP/HTTPS web URLs, including multi-level subdomains,
     * optional ports, paths, query strings, and URL fragments.
     */
    val urlRegex = Regex(
        "^(https?://)?([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}(:\\d{1,5})?([/?#][^\\s]*)?$"
    )

    /**
     * Regular expression for validating usernames (3 to 20 alphanumeric characters or `_`).
     */
    val usernameRegex = Regex("^[a-zA-Z0-9_]{3,20}$")

    /**
     * Regular expression for validating Persian/Arabic script text, including spaces and ZWNJ (`\u200C`).
     */
    val persianTextRegex = Regex("^[\\u0600-\\u06FF\\u200C\\s]+$")

    /**
     * Regular expression for validating English letters (`a-z`, `A-Z`) and spaces.
     */
    val englishTextRegex = Regex("^[a-zA-Z\\s]+$")

    /**
     * Regular expression for CSS / Android Hex color codes (`#RGB`, `#RRGGBB`, or `#AARRGGBB`).
     */
    val hexColorRegex = Regex("^#(?:[0-9a-fA-F]{3}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})$")

    /**
     * Regular expression for IPv4 addresses (`0.0.0.0` to `255.255.255.255`).
     */
    val ipv4Regex = Regex(
        "^(?:(?:25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$"
    )

    /**
     * Regular expression for canonical UUID strings (`8-4-4-4-12` hexadecimal).
     */
    val uuidRegex = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")

    /**
     * Regular expression for URL-friendly lowercase slugs (`my-article-title-2`).
     */
    val slugRegex = Regex("^[a-z0-9]+(?:-[a-z0-9]+)*$")
}
