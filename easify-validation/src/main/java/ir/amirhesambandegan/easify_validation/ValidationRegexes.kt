package ir.amirhesambandegan.easify_validation

/**
 * A comprehensive singleton object containing common Regular Expressions used for data validation across the application.
 * These regex patterns cover typical validation scenarios such as emails, passwords, phone numbers, and regional formats like Iranian national IDs and mobile numbers.
 */
object ValidationRegexes {

    /**
     * A regular expression for validating standard email addresses.
     * Ensures the email format has a valid prefix, an '@' symbol, a domain name, and a domain extension of at least 2 characters.
     */
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

    /**
     * A regular expression for validating Iranian mobile numbers.
     * The number must start with '09' followed exactly by 9 digits, making a total of 11 digits.
     */
    val mobileRegex = Regex("^09\\d{9}\$")

    /**
     * A regular expression for validating Iranian National Identity numbers (Code Melli).
     * The code must consist of exactly 10 numeric digits. Note: This does not validate the checksum logic of the national ID.
     */
    val nationalIdRegex = Regex("^\\d{10}\$")

    /**
     * A regular expression for validating strong passwords.
     * The password must meet the following criteria:
     * - Minimum 8 characters in length
     * - Contains at least one uppercase English letter (A-Z)
     * - Contains at least one lowercase English letter (a-z)
     * - Contains at least one numeric digit (0-9)
     * - Contains at least one special character from the set: @, $, !, %, *, ?, &
     */
    val passwordStrongRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")

    /**
     * A regular expression for validating Iranian postal codes.
     * The postal code must consist of exactly 10 numeric digits.
     */
    val postalCodeRegex = Regex("^\\d{10}\$")

    /**
     * A regular expression for validating Iranian landline phone numbers including the area code.
     * The number must start with '0' followed by exactly 10 digits (e.g., 02188888888).
     */
    val landlineRegex = Regex("^0\\d{10}\$")

    /**
     * A regular expression for validating standard web URLs.
     * It matches URLs with or without http/https protocols, with or without 'www.', and includes domain and optional path/query parameters.
     */
    val urlRegex = Regex("^(https?://)?(www\\.)?([\\w\\d-]+)\\.([\\w\\d]{2,})(/[\\w\\d-./?%&=]*)?\$")

    /**
     * A regular expression for validating usernames.
     * The username must be between 3 and 20 characters long and can only contain alphanumeric characters (a-z, A-Z, 0-9) and underscores (_).
     */
    val usernameRegex = Regex("^[a-zA-Z0-9_]{3,20}\$")

    /**
     * A regular expression for validating that a given string contains only Persian/Arabic characters and spaces.
     * This checks against the Unicode range \u0600-\u06FF.
     */
    val persianTextRegex = Regex("^[\\u0600-\\u06FF\\s]+\$")

    /**
     * A regular expression for validating that a given string contains only standard English letters (a-z, A-Z) and spaces.
     */
    val englishTextRegex = Regex("^[a-zA-Z\\s]+\$")
}
