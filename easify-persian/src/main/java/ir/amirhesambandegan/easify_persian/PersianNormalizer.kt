package ir.amirhesambandegan.easify_persian

/**
 * Standardizes Persian text by fixing Arabic characters, half-spaces, and digits.
 * Replaces Arabic variants of letters (e.g., 'ي' to 'ی', 'ك' to 'ک') and removes Arabic diacritics.
 * It also converts any English digits to Persian digits.
 *
 * @return The normalized Persian string.
 */
fun String.toStandardPersian(): String {
    return this
        .replace("ي", "ی")
        .replace("ك", "ک")
        .replace("ؤ", "و")
        .replace("إ", "ا")
        .replace("أ", "ا")
        .replace("ة", "ه")
        .replace(Regex("[\u064B-\u0652]"), "") // Remove Arabic vowels/diacritics
        .toPersianDigits()
}
