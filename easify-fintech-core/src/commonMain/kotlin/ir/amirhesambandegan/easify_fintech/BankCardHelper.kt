package ir.amirhesambandegan.easify_fintech

/**
 * Validates a 16-digit bank card number using the Luhn algorithm (normalizes Persian/Arabic digits).
 */
fun String.isValidCardNumber(): Boolean {
    val cleanNumber = toEnglishNumbers().filter { it in '0'..'9' }
    if (cleanNumber.length != 16) return false

    var sum = 0
    var alternate = false
    for (i in cleanNumber.length - 1 downTo 0) {
        var n = cleanNumber[i] - '0'
        if (alternate) {
            n *= 2
            if (n > 9) n -= 9
        }
        sum += n
        alternate = !alternate
    }
    return sum % 10 == 0
}

/**
 * Formats a bank card number string into grouped format (`XXXX XXXX XXXX XXXX`).
 */
fun String.formatCardNumber(separator: String = " "): String {
    val clean = toEnglishNumbers().filter { it in '0'..'9' }
    return clean.chunked(4).joinToString(separator)
}

/**
 * Masks the middle digits of a 16-digit card number (e.g. `6037 99** **** 1234`).
 */
fun String.maskCardNumber(maskChar: Char = '*', separator: String = " "): String {
    val clean = toEnglishNumbers().filter { it in '0'..'9' }
    if (clean.length != 16) return this
    val mask4 = maskChar.toString().repeat(4)
    val mask2 = maskChar.toString().repeat(2)
    return "${clean.substring(0, 4)}$separator${clean.substring(4, 6)}$mask2$separator$mask4$separator${clean.substring(12, 16)}"
}

/**
 * Returns the bank name based on the 6-digit BIN (Bank Identification Number).
 */
fun String.getBankName(): String? {
    val clean = toEnglishNumbers().filter { it in '0'..'9' }
    if (clean.length < 6) return null
    val bin = clean.substring(0, 6)
    return when (bin) {
        "603799", "170019" -> "Bank Melli"
        "589210" -> "Bank Sepah"
        "627648", "207177" -> "Bank Tosee Saderat"
        "627961" -> "Bank Sanat va Maadan"
        "603770" -> "Bank Keshavarzi"
        "628023" -> "Bank Maskan"
        "627760" -> "Post Bank"
        "628157" -> "Tosee Credit Institution"
        "502908" -> "Bank Tosee Taavon"
        "627412" -> "Bank Eghtesad Novin"
        "622106" -> "Bank Parsian"
        "639194" -> "Bank Pasargad"
        "621986" -> "Bank Saman"
        "639346" -> "Bank Sina"
        "639607" -> "Bank Sarmayeh"
        "627488" -> "Bank Karafarin"
        "502931" -> "Bank Day"
        "636214" -> "Bank Ayandeh"
        "627353", "585983" -> "Bank Tejarat"
        "610433" -> "Bank Mellat"
        "603769" -> "Bank Saderat"
        "589463" -> "Bank Refah"
        "627381" -> "Bank Ansar"
        "639370" -> "Bank Mehr Eghtesad"
        "606373" -> "Bank Qarzolhasaneh Mehr Iran"
        "504172" -> "Bank Qarzolhasaneh Resalat"
        "504706" -> "Bank Shahr"
        "505416" -> "Bank Gardeshgari"
        "505785" -> "Bank Iran Zamin"
        "505801" -> "Kousar Credit Institution"
        "505809" -> "Middle East Bank"
        "606256" -> "Melal Credit Institution"
        "636949" -> "Bank Hekmat"
        "636795" -> "Markazi"
        else -> null
    }
}
