package ir.amirhesambandegan.easify_persian

private val PERSIAN_DIGITS = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
private val ARABIC_DIACRITICS_REGEX = Regex("[\u064B-\u065F\u0670]")

/**
 * Converts all ASCII (`0-9`) and Eastern Arabic (`٠-٩`) digits in the string to Persian digits (`۰-۹`).
 */
fun String.toPersianDigits(): String {
    val sb = StringBuilder(length)
    for (ch in this) {
        when (ch) {
            in '0'..'9' -> sb.append(PERSIAN_DIGITS[ch - '0'])
            in '\u0660'..'\u0669' -> sb.append(PERSIAN_DIGITS[ch.code - 0x0660])
            else -> sb.append(ch)
        }
    }
    return sb.toString()
}

/**
 * Converts all Persian (`۰-۹`) and Eastern Arabic (`٠-٩`) digits in the string to ASCII English digits (`0-9`).
 */
fun String.toEnglishDigits(): String {
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
 * Normalizes Arabic letter variants (`ي` -> `ی`, `ك` -> `ک`, etc.) and strips Arabic vowel diacritics
 * while preserving the existing digits as they are.
 */
fun String.normalizePersianLetters(): String {
    val sb = StringBuilder(length)
    for (ch in this) {
        when (ch) {
            'ي', 'ئ' -> sb.append(if (ch == 'ي') 'ی' else ch)
            'ك' -> sb.append('ک')
            'ؤ' -> sb.append('و')
            'إ', 'أ' -> sb.append('ا')
            'ة' -> sb.append('ه')
            else -> sb.append(ch)
        }
    }
    return sb.toString().replace(ARABIC_DIACRITICS_REGEX, "")
}

/**
 * Standardizes Persian text by normalizing Arabic letter variants, removing Arabic diacritics,
 * and converting digits to Persian digits (`۰-۹`).
 */
fun String.toStandardPersian(): String = normalizePersianLetters().toPersianDigits()
