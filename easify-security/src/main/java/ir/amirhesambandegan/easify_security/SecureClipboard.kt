package ir.amirhesambandegan.easify_security

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object SecureClipboard {

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
