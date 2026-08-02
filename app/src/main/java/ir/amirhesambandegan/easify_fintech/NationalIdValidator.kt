package ir.amirhesambandegan.easify_fintech

/**
 * Validates an Iranian National ID (Code Melli) using the standard checksum algorithm.
 *
 * @return True if the National ID is mathematically valid, false otherwise.
 */
fun String.isValidNationalId(): Boolean {
    if (this.length != 10) return false
    if (!this.all { it.isDigit() }) return false
    
    // Check if all digits are the same (e.g., 1111111111 is invalid)
    if (this.all { it == this[0] }) return false

    val digits = this.map { it.toString().toInt() }
    val checkDigit = digits[9]
    val sum = (0 until 9).sumOf { i ->
        digits[i] * (10 - i)
    }
    
    val remainder = sum % 11
    
    return if (remainder < 2) {
        checkDigit == remainder
    } else {
        checkDigit == (11 - remainder)
    }
}
