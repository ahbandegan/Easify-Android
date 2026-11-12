package ir.amirhesambandegan.easify_format

private val NON_ALNUM_REGEX = Regex("[^a-zA-Z0-9]+")
private val CAMEL_BOUNDARY_REGEX = Regex("([a-z0-9])([A-Z])")
private val MULTI_WHITESPACE_REGEX = Regex("\\s+")
private val SLUG_INVALID_REGEX = Regex("[^a-zA-Z0-9\u0622-\u06CC]+")
private val MULTI_HYPHEN_REGEX = Regex("-+")

/**
 * Converts a string to Title Case, capitalizing the first letter of each whitespace-separated word.
 */
fun String.toTitleCase(): String {
    if (isBlank()) return ""
    return this.lowercase()
        .trim()
        .split(MULTI_WHITESPACE_REGEX)
        .filter { it.isNotEmpty() }
        .joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecaseChar().toString() else it.toString() }
        }
}

/**
 * Converts a string to camelCase, removing non-alphanumeric separators.
 */
fun String.toCamelCase(): String {
    val words = this.replace(NON_ALNUM_REGEX, " ")
        .trim()
        .split(MULTI_WHITESPACE_REGEX)
        .filter { it.isNotEmpty() }
    if (words.isEmpty()) return ""
    return words.first().lowercase() + words.drop(1).joinToString("") { word ->
        word.lowercase().replaceFirstChar { it.uppercaseChar().toString() }
    }
}

/**
 * Converts a string to snake_case.
 */
fun String.toSnakeCase(): String {
    return this.replace(CAMEL_BOUNDARY_REGEX, "$1_$2")
        .replace(NON_ALNUM_REGEX, "_")
        .lowercase()
        .trim('_')
}

/**
 * Converts a string into a URL-friendly slug supporting ASCII and Persian letters.
 */
fun String.toSlug(): String {
    return this.replace(SLUG_INVALID_REGEX, "-")
        .replace(MULTI_HYPHEN_REGEX, "-")
        .trim('-')
        .lowercase()
}

/**
 * Truncates the string cleanly to [maxLength] appending [ellipsis] when truncated.
 */
fun String.truncate(maxLength: Int, ellipsis: String = "..."): String {
    if (maxLength <= 0) return ""
    if (this.length <= maxLength) return this
    if (maxLength <= ellipsis.length) return this.take(maxLength)
    return this.take(maxLength - ellipsis.length) + ellipsis
}
