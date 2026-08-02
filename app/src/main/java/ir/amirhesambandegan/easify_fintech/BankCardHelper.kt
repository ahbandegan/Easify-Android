package ir.amirhesambandegan.easify_fintech

/**
 * Validates a 16-digit bank card number using the Luhn algorithm.
 */
fun String.isValidCardNumber(): Boolean {
    val cleanNumber = this.filter { it.isDigit() }
    if (cleanNumber.length != 16) return false
    
    var sum = 0
    var alternate = false
    for (i in cleanNumber.length - 1 downTo 0) {
        var n = cleanNumber[i].toString().toInt()
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
 * Formats a digits-only string into a card number format (XXXX XXXX XXXX XXXX).
 */
fun String.formatCardNumber(): String {
    val clean = this.filter { it.isDigit() }
    return clean.chunked(4).joinToString(" ")
}

/**
 * Returns the bank name based on the 6-digit BIN (Bank Identification Number).
 * Returns null if the BIN is not recognized.
 */
fun String.getBankName(): String? {
    if (this.length < 6) return null
    val bin = this.substring(0, 6)
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
