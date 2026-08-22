package ir.amirhesambandegan.easify_format

fun String.maskEmail(): String {
    val parts = this.split("@")
    if (parts.size != 2) return this
    val name = parts[0]
    val domain = parts[1]
    
    val maskedName = if (name.length <= 3) {
        name.first() + "***"
    } else {
        name.take(3) + "***"
    }
    return "$maskedName@$domain"
}

fun String.maskPhone(): String {
    val digits = this.filter { it.isDigit() }
    if (digits.length < 7) return this
    val start = digits.take(4)
    val end = digits.takeLast(4)
    return "$start***$end"
}

fun String.maskCard(): String {
    val digits = this.filter { it.isDigit() }
    if (digits.length != 16) return this
    val start = digits.take(4)
    val end = digits.takeLast(4)
    return "$start **** **** $end"
}
