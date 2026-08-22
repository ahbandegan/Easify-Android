package ir.amirhesambandegan.easify_form

object InputFilters {
    /** Allows only digits. */
    fun onlyNumbers(): (String) -> String = { it.filter { char -> char.isDigit() } }
    
    /** Allows only English letters and spaces. */
    fun onlyEnglishLetters(): (String) -> String = { it.filter { char -> char in 'a'..'z' || char in 'A'..'Z' || char == ' ' } }
    
    /** Allows only Persian letters and spaces/zwnj. */
    fun onlyPersianLetters(): (String) -> String = { it.filter { char -> char in 'آ'..'ی' || char == ' ' || char == '‌' } }
    
    /** Removes all spaces. */
    fun noSpaces(): (String) -> String = { it.replace(" ", "") }
}
