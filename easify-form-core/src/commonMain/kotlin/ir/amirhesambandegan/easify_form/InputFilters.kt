package ir.amirhesambandegan.easify_form

/**
 * Utility object offering common predefined multiplatform input filters.
 */
object InputFilters {

    /**
     * Converts Persian/Arabic-Indic digits (`۰-۹`, `٠-٩`) in [input] to ASCII digits (`0-9`).
     */
    fun normalizeDigits(input: String): String {
        val sb = StringBuilder(input.length)
        for (c in input) {
            val mapped = when (c) {
                in '۰'..'۹' -> ('0'.code + (c - '۰')).toChar()
                in '٠'..'٩' -> ('0'.code + (c - '٠')).toChar()
                else -> c
            }
            sb.append(mapped)
        }
        return sb.toString()
    }

    /** Allows only digits (converting Persian/Arabic digits to ASCII if present). */
    fun onlyNumbers(): (String) -> String = { input ->
        normalizeDigits(input).filter { it in '0'..'9' }
    }

    /** Converts Persian/Arabic digits to ASCII digits while preserving other characters. */
    fun normalizedDigits(): (String) -> String = { input ->
        normalizeDigits(input)
    }

    /** Limits the input length to at most [maxLength] characters. */
    fun limitLength(maxLength: Int): (String) -> String = { input ->
        if (maxLength <= 0) "" else if (input.length > maxLength) input.substring(0, maxLength) else input
    }

    /** Allows only English alphabetic letters and spaces. */
    fun onlyEnglishLetters(): (String) -> String = {
        it.filter { char -> char in 'a'..'z' || char in 'A'..'Z' || char == ' ' }
    }

    /** Allows only Persian (Farsi) characters, spaces, and zero-width non-joiners. */
    fun onlyPersianLetters(): (String) -> String = {
        it.filter { char -> char in 'آ'..'ی' || char == ' ' || char == '\u200C' }
    }

    /** Removes all whitespace spaces from the input. */
    fun noSpaces(): (String) -> String = { it.replace(" ", "") }
}
