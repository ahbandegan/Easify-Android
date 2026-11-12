package ir.amirhesambandegan.easify_format

private val HTML_BREAK_REGEX = Regex("<(?:br\\s*/?|/p|/div|/li)>", RegexOption.IGNORE_CASE)
private val HTML_TAG_REGEX = Regex("<[^>]*>")
private val HTML_NUMERIC_ENTITY_REGEX = Regex("&#(x?[0-9a-fA-F]+);")
private val WHITESPACE_SPLIT_REGEX = Regex("\\s+")
private val URL_EXTRACT_REGEX = Regex("https?://[\\w-]+(?:\\.[\\w-]+)+(?::\\d{1,5})?(?:[/?#][^\\s\"'<>]*)?")
private val EMAIL_EXTRACT_REGEX = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")

/**
 * Strips all HTML tags from the string and unescapes common HTML entities, returning clean plain text.
 * Pure Kotlin Multiplatform implementation.
 */
fun String.stripHtmlTags(): String {
    if (isEmpty()) return this
    val withBreaks = replace(HTML_BREAK_REGEX, "\n")
    val withoutTags = withBreaks.replace(HTML_TAG_REGEX, "")
    val decodedNamed = withoutTags
        .replace("&nbsp;", " ")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&quot;", "\"")
        .replace("&#39;", "'")
        .replace("&apos;", "'")

    return HTML_NUMERIC_ENTITY_REGEX.replace(decodedNamed) { match ->
        val raw = match.groupValues[1]
        val codePoint = if (raw.startsWith("x", ignoreCase = true)) {
            raw.drop(1).toIntOrNull(16)
        } else {
            raw.toIntOrNull(10)
        }
        if (codePoint != null && codePoint in 1..Char.MAX_VALUE.code) {
            codePoint.toChar().toString()
        } else {
            match.value
        }
    }.trim()
}

/**
 * Extracts uppercase initials from the string.
 */
fun String.toInitials(): String {
    val words = trim().split(WHITESPACE_SPLIT_REGEX).filter { it.isNotEmpty() }
    return when {
        words.isEmpty() -> ""
        words.size == 1 -> words[0].take(2).uppercase()
        else -> "${words.first().first()}${words.last().first()}".uppercase()
    }
}

/**
 * Extracts all HTTP/HTTPS URLs found inside the string in order of appearance.
 */
fun String.extractUrls(): List<String> =
    URL_EXTRACT_REGEX.findAll(this).map { it.value }.toList()

/**
 * Extracts all email addresses found inside the string in order of appearance.
 */
fun String.extractEmails(): List<String> =
    EMAIL_EXTRACT_REGEX.findAll(this).map { it.value }.toList()

/**
 * Counts whitespace-delimited words in the string.
 */
fun String.wordCount(): Int =
    if (isBlank()) 0 else trim().split(WHITESPACE_SPLIT_REGEX).count { it.isNotEmpty() }
