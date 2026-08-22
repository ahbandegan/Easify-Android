package ir.amirhesambandegan.easify_form

/**
 * A utility object offering common predefined input filters.
 * Filters can be applied to fields to intercept and modify user input dynamically.
 */
object InputFilters {
    /** 
     * Allows only numeric digits. Strips out all non-digit characters.
     *
     * @return A filter function to retain only numbers.
     */
    fun onlyNumbers(): (String) -> String = { it.filter { char -> char.isDigit() } }
    
    /** 
     * Allows only English alphabetic letters and spaces.
     *
     * @return A filter function to retain only English letters and spaces.
     */
    fun onlyEnglishLetters(): (String) -> String = { it.filter { char -> char in 'a'..'z' || char in 'A'..'Z' || char == ' ' } }
    
    /** 
     * Allows only Persian (Farsi) characters, spaces, and zero-width non-joiners (zwnj).
     *
     * @return A filter function to retain only Persian letters, spaces, and half-spaces.
     */
    fun onlyPersianLetters(): (String) -> String = { it.filter { char -> char in 'آ'..'ی' || char == ' ' || char == '‌' } }
    
    /** 
     * Removes all space characters from the input.
     *
     * @return A filter function that eliminates all spaces.
     */
    fun noSpaces(): (String) -> String = { it.replace(" ", "") }
}
