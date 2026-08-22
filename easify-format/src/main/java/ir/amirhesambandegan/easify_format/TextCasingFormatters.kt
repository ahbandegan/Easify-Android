package ir.amirhesambandegan.easify_format

import java.util.Locale

fun String.toTitleCase(): String {
    return this.lowercase(Locale.getDefault())
        .split(" ")
        .joinToString(" ") { word -> 
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } 
        }
}

fun String.toCamelCase(): String {
    val words = this.replace(Regex("[^a-zA-Z0-9]+"), " ").trim().split(" ")
    if (words.isEmpty()) return ""
    return words.first().lowercase() + words.drop(1).joinToString("") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
}

fun String.toSnakeCase(): String {
    return this.replace(Regex("([a-z])([A-Z]+)"), "$1_$2")
        .replace(Regex("[^a-zA-Z0-9]+"), "_")
        .lowercase()
        .trim('_')
}

fun String.toSlug(): String {
    return this.replace(Regex("[^a-zA-Z0-9ا-ی]+"), "-")
        .replace(Regex("-+"), "-")
        .trim('-')
        .lowercase()
}
