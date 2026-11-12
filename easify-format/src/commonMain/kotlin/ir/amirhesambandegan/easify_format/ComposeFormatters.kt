package ir.amirhesambandegan.easify_format

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

private val LINK_URL_REGEX = Regex("https?://[\\w-]+(?:\\.[\\w-]+)+(?::\\d{1,5})?(?:[/?#][^\\s\"'<>]*)?")
private val LINK_EMAIL_REGEX = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
private val LINK_MENTION_REGEX = Regex("@\\w+")

/**
 * Highlights all occurrences of [query] within the string and returns an [AnnotatedString].
 *
 * @param query The text to search for within the string.
 * @param highlightColor The color to apply to matching portions. Defaults to [Color.Yellow].
 * @param fontWeight The font weight to apply to matching portions. Defaults to [FontWeight.Bold].
 * @param ignoreCase Whether matching is case-insensitive. Defaults to `true`.
 * @return An [AnnotatedString] with every occurrence highlighted.
 */
fun String.highlightMatch(
    query: String,
    highlightColor: Color = Color.Yellow,
    fontWeight: FontWeight = FontWeight.Bold,
    ignoreCase: Boolean = true
): AnnotatedString {
    val source = this
    if (query.isBlank()) return AnnotatedString(source)
    val firstIndex = source.indexOf(query, ignoreCase = ignoreCase)
    if (firstIndex == -1) return AnnotatedString(source)

    return buildAnnotatedString {
        var currentIndex = 0
        var matchIndex = firstIndex
        while (matchIndex != -1) {
            if (matchIndex > currentIndex) {
                append(source.substring(currentIndex, matchIndex))
            }
            withStyle(style = SpanStyle(color = highlightColor, fontWeight = fontWeight)) {
                append(source.substring(matchIndex, matchIndex + query.length))
            }
            currentIndex = matchIndex + query.length
            matchIndex = source.indexOf(query, startIndex = currentIndex, ignoreCase = ignoreCase)
        }
        if (currentIndex < source.length) {
            append(source.substring(currentIndex))
        }
    }
}

/**
 * Applies a custom [SpanStyle] to every segment matching [pattern] inside the string.
 */
fun String.stylePattern(
    pattern: Regex,
    style: SpanStyle
): AnnotatedString {
    val source = this
    val matches = pattern.findAll(source).toList()
    if (matches.isEmpty()) return AnnotatedString(source)

    return buildAnnotatedString {
        var currentIndex = 0
        for (match in matches) {
            if (match.range.first < currentIndex) continue
            if (match.range.first > currentIndex) {
                append(source.substring(currentIndex, match.range.first))
            }
            withStyle(style) {
                append(match.value)
            }
            currentIndex = match.range.last + 1
        }
        if (currentIndex < source.length) {
            append(source.substring(currentIndex))
        }
    }
}

/**
 * Converts the string into an [AnnotatedString] where URLs, email addresses, and `@mentions`
 * are highlighted and annotated with the `"URL"` string annotation tag.
 *
 * @param linkColor The color to apply to linkified text. Defaults to [Color.Blue].
 * @return An [AnnotatedString] with annotated links, emails, and mentions.
 */
fun String.toLinkifiedAnnotatedString(linkColor: Color = Color.Blue): AnnotatedString {
    val source = this
    val matches = (
        LINK_URL_REGEX.findAll(source) +
            LINK_EMAIL_REGEX.findAll(source) +
            LINK_MENTION_REGEX.findAll(source)
        )
        .sortedWith(
            compareBy<MatchResult> { it.range.first }
                .thenByDescending { it.range.last - it.range.first }
        )
        .toList()

    if (matches.isEmpty()) return AnnotatedString(source)

    return buildAnnotatedString {
        var currentIndex = 0
        for (match in matches) {
            if (match.range.first >= currentIndex) {
                if (match.range.first > currentIndex) {
                    append(source.substring(currentIndex, match.range.first))
                }
                pushStringAnnotation(tag = "URL", annotation = match.value)
                withStyle(style = SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline)) {
                    append(match.value)
                }
                pop()
                currentIndex = match.range.last + 1
            }
        }
        if (currentIndex < source.length) {
            append(source.substring(currentIndex))
        }
    }
}
