package ir.amirhesambandegan.easify_format

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

/**
 * Highlights all occurrences of a specific query within the string and returns an [AnnotatedString].
 *
 * @param query The text to search for within the string.
 * @param highlightColor The color to apply to the matching text. Defaults to [Color.Yellow].
 * @param fontWeight The font weight to apply to the matching text. Defaults to [FontWeight.Bold].
 * @return An [AnnotatedString] with the matching portions highlighted, or the original string
 * wrapped in an [AnnotatedString] if the query is blank or no match is found.
 */
fun String.highlightMatch(query: String, highlightColor: Color = Color.Yellow, fontWeight: FontWeight = FontWeight.Bold): AnnotatedString {
    if (query.isBlank()) return AnnotatedString(this)
    val startIndex = this.indexOf(query, ignoreCase = true)
    if (startIndex == -1) return AnnotatedString(this)

    return buildAnnotatedString {
        append(this@highlightMatch.substring(0, startIndex))
        withStyle(style = SpanStyle(color = highlightColor, fontWeight = fontWeight)) {
            append(this@highlightMatch.substring(startIndex, startIndex + query.length))
        }
        append(this@highlightMatch.substring(startIndex + query.length))
    }
}

/**
 * Converts the string into an [AnnotatedString] where URLs, email addresses, and mentions
 * are highlighted and annotated.
 *
 * @param linkColor The color to apply to the linkified text. Defaults to [Color.Blue].
 * @return An [AnnotatedString] with clickable links, emails, and mentions formatted with
 * an underline and the specified link color.
 */
fun String.toLinkifiedAnnotatedString(linkColor: Color = Color.Blue): AnnotatedString {
    val urlRegex = Regex("(https?://[\\w-]+(\\.[\\w-]+)+(/[^\\s]*)?)")
    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    val mentionRegex = Regex("@\\w+")

    return buildAnnotatedString {
        var currentIndex = 0
        val matches = (urlRegex.findAll(this@toLinkifiedAnnotatedString) + 
                       emailRegex.findAll(this@toLinkifiedAnnotatedString) + 
                       mentionRegex.findAll(this@toLinkifiedAnnotatedString))
                      .sortedBy { it.range.first }
                      .toList()

        for (match in matches) {
            if (match.range.first >= currentIndex) {
                append(this@toLinkifiedAnnotatedString.substring(currentIndex, match.range.first))
                
                pushStringAnnotation(tag = "URL", annotation = match.value)
                withStyle(style = SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline)) {
                    append(match.value)
                }
                pop()
                
                currentIndex = match.range.last + 1
            }
        }
        if (currentIndex < this@toLinkifiedAnnotatedString.length) {
            append(this@toLinkifiedAnnotatedString.substring(currentIndex))
        }
    }
}
