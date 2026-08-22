package ir.amirhesambandegan.easify_format

import android.os.Build
import android.text.Html

/**
 * Strips all HTML tags from the string, returning plain text.
 *
 * @return The string with all HTML tags removed.
 */
fun String.stripHtmlTags(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this).toString()
    }
}

/**
 * Extracts the initials from the string. If the string contains one word, it returns
 * the first two letters. If it contains multiple words, it returns the first letter
 * of the first and last words. All initials are capitalized.
 *
 * @return The initials extracted from the string.
 */
fun String.toInitials(): String {
    val words = this.trim().split(Regex("\\s+"))
    return when {
        words.isEmpty() -> ""
        words.size == 1 -> words[0].take(2).uppercase()
        else -> "${words.first().first()}${words.last().first()}".uppercase()
    }
}
