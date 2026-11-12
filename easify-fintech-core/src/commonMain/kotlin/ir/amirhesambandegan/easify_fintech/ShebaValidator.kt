package ir.amirhesambandegan.easify_fintech

/**
 * Validates an Iranian SHEBA (IBAN) number using pure-KMP ISO 7064 Mod 97-10 arithmetic.
 */
fun String.isValidSheba(): Boolean {
    val clean = toEnglishNumbers().uppercase().replace(" ", "").replace("-", "")
    if (clean.length != 26 || !clean.startsWith("IR")) return false
    if (!clean.substring(2).all { it in '0'..'9' }) return false

    // Move first 4 chars to the end; I=18, R=27 -> "1827"
    val rearranged = clean.substring(4) + "1827" + clean.substring(2, 4)
    var remainder = 0
    for (ch in rearranged) {
        remainder = (remainder * 10 + (ch - '0')) % 97
    }
    return remainder == 1
}

/**
 * Formats a valid or partial Iranian SHEBA string into space-separated 4-character blocks
 * (e.g. `IR12 0170 0000 0010 1234 5678 90`).
 */
fun String.formatSheba(separator: String = " "): String {
    val clean = toEnglishNumbers().uppercase().replace(" ", "").replace("-", "")
    return clean.chunked(4).joinToString(separator)
}

/**
 * Identifies the bank name from the 3-digit bank code in the SHEBA.
 */
fun String.getShebaBankName(): String? {
    val clean = toEnglishNumbers().uppercase().replace(" ", "").replace("-", "")
    if (clean.length < 7 || !clean.startsWith("IR")) return null
    val bankCode = clean.substring(4, 7)
    return when (bankCode) {
        "010" -> "Bank Markazi"
        "011" -> "Bank Sanat va Maadan"
        "012" -> "Bank Mellat"
        "013" -> "Bank Refah"
        "014" -> "Bank Maskan"
        "015" -> "Bank Sepah"
        "016" -> "Bank Keshavarzi"
        "017" -> "Bank Melli"
        "018" -> "Bank Tejarat"
        "019" -> "Bank Saderat"
        "020" -> "Bank Tosee Saderat"
        "021" -> "Bank Post Bank"
        "022" -> "Bank Tosee Taavon"
        "051" -> "Bank Tosee"
        "052" -> "Bank Qavamin"
        "053" -> "Bank Karafarin"
        "054" -> "Bank Parsian"
        "055" -> "Bank Eghtesad Novin"
        "056" -> "Bank Saman"
        "057" -> "Bank Pasargad"
        "058" -> "Bank Sarmayeh"
        "059" -> "Bank Sina"
        "060" -> "Bank Mehr Iran"
        "061" -> "Bank Shahr"
        "062" -> "Bank Ayandeh"
        "063" -> "Bank Ansar"
        "064" -> "Bank Gardeshgari"
        "065" -> "Bank Hekmat"
        "066" -> "Bank Dey"
        "069" -> "Bank Iran Zamin"
        "070" -> "Bank Resalat"
        "073" -> "Bank Kowsar"
        "075" -> "Bank Melal"
        "078" -> "Bank Middle East"
        else -> null
    }
}
