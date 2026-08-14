package ir.amirhesambandegan.easify_context

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Copies the specified text to the system clipboard.
 *
 * @param text The text to be copied to the clipboard.
 * @param label A user-visible label for the clip data. Defaults to "EasifyData".
 */
fun Context.copyToClipboard(text: String, label: String = "EasifyData") {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}
