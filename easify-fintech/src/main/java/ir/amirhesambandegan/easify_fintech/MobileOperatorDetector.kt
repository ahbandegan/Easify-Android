package ir.amirhesambandegan.easify_fintech

/**
 * Detects the Iranian mobile operator based on the phone number prefix.
 *
 * @return The name of the operator (e.g., "MCI", "Irancell", "Rightel") or null if not recognized.
 */
fun String.getMobileOperator(): String? {
    val clean = this.replace("+98", "0").filter { it.isDigit() }
    if (clean.length < 4) return null
    val prefix = clean.substring(0, 4)
    
    return when (prefix) {
        "0910", "0911", "0912", "0913", "0914", "0915", "0916", "0917", "0918", "0919",
        "0990", "0991", "0992", "0993", "0994", "0996" -> "MCI"
        
        "0930", "0933", "0935", "0936", "0937", "0938", "0939",
        "0901", "0902", "0903", "0904", "0905", "0941" -> "Irancell"
        
        "0920", "0921", "0922", "0923" -> "Rightel"
        
        "0998" -> "Shatel Mobile"
        "0999" -> "Samantel / Aptel"
        "0932" -> "Taliya"
        "0934" -> "TeleKish"
        else -> null
    }
}
