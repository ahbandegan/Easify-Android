package ir.amirhesambandegan.easify_persian

/**
 * Converts all English digits in the string to Persian digits.
 * Example: "123" -> "۱۲۳"
 *
 * @return A new string with Persian digits.
 */
fun String.toPersianDigits(): String {
    var result = this
    val englishDigits = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val persianDigits = arrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    
    for (i in englishDigits.indices) {
        result = result.replace(englishDigits[i], persianDigits[i])
    }
    return result
}
