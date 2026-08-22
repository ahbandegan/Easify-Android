package ir.amirhesambandegan.easify_format

import java.util.Locale

/**
 * Converts a string to Title Case, where the first letter of each word is capitalized.
 *
 * @return The string formatted in Title Case.
 */
fun String.toTitleCase(): String {
    return this.lowercase(Locale.getDefault())
        .split(" ")
        .joinToString(" ") { word -> 
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } 
        }
}

/**
 * Converts a string to camelCase, removing special characters and capitalizing the first
 * letter of each subsequent word.
 *
 * @return The string formatted in camelCase.
 */
fun String.toCamelCase(): String {
    val words = this.replace(Regex("[^a-zA-Z0-9]+"), " ").trim().split(" ")
    if (words.isEmpty()) return ""
    return words.first().lowercase() + words.drop(1).joinToString("") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
}

/**
 * Converts a string to snake_case, replacing spaces and uppercase letters with underscores.
 *
 * @return The string formatted in snake_case.
 */
fun String.toSnakeCase(): String {
    return this.replace(Regex("([a-z])([A-Z]+)"), "$1_$2")
        .replace(Regex("[^a-zA-Z0-9]+"), "_")
        .lowercase()
        .trim('_')
}

/**
 * Converts a string into a URL-friendly slug, replacing non-alphanumeric characters with hyphens.
 *
 * @return The string formatted as a slug.
 */
fun String.toSlug(): String {
    return this.replace(Regex("[^a-zA-Z0-9ا-ی]+"), "-")
        .replace(Regex("-+"), "-")
        .trim('-')
        .lowercase()
}
