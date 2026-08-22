package ir.amirhesambandegan.easify_security

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A utility object to securely handle clipboard operations, minimizing the risk
 * of sensitive data exposure through the system clipboard.
 */
object SecureClipboard {

    /**
     * Copies sensitive text to the system clipboard and schedules its removal
     * after a specified duration to ensure privacy.
     *
     * @param context The application context used to access the clipboard service.
     * @param label The user-visible label for the clip data.
     * @param text The sensitive text to be copied to the clipboard.
     * @param clearAfterMillis The duration in milliseconds before the clipboard is automatically cleared. Defaults to 30,000ms (30 seconds).
     * @param scope The coroutine scope used to launch the delayed clearing task. Defaults to a scope using [Dispatchers.Main].
     */
    fun copySensitiveText(
        context: Context,
        label: String,
        text: String,
        clearAfterMillis: Long = 30000L,
        scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    ) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)

        scope.launch {
            delay(clearAfterMillis)
            if (clipboard.primaryClip?.getItemAt(0)?.text?.toString() == text) {
                clipboard.setPrimaryClip(ClipData.newPlainText("Cleared", ""))
            }
        }
    }
}
