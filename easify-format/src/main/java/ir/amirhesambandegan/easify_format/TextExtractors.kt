package ir.amirhesambandegan.easify_format

import android.os.Build
import android.text.Html

fun String.stripHtmlTags(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this).toString()
    }
}

fun String.toInitials(): String {
    val words = this.trim().split(Regex("\\s+"))
    return when {
        words.isEmpty() -> ""
        words.size == 1 -> words[0].take(2).uppercase()
        else -> "${words.first().first()}${words.last().first()}".uppercase()
    }
}
