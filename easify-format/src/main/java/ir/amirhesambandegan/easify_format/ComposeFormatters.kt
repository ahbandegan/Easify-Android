package ir.amirhesambandegan.easify_format

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

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
