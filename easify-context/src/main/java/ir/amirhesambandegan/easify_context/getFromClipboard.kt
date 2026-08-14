package ir.amirhesambandegan.easify_context

import android.content.ClipboardManager
import android.content.Context

/**
 * Retrieves the current text content from the system clipboard.
 *
 * @return The text content of the primary clip, or null if the clipboard is empty or does not contain text.
 */
fun Context.getFromClipboard(): String? {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    if (!clipboard.hasPrimaryClip()) return null
    
    val item = clipboard.primaryClip?.getItemAt(0)
    return item?.text?.toString()
}
