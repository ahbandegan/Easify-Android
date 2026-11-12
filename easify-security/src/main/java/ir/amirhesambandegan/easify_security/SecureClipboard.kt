package ir.amirhesambandegan.easify_security

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Handles sensitive clipboard operations by masking OS clipboard previews on Android 13+
 * (`ClipDescription.EXTRA_IS_SENSITIVE`) and automatically clearing the clip after a delay.
 */
object SecureClipboard {

    private val defaultScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @Volatile
    private var clearJob: Job? = null

    /**
     * Copies sensitive [text] to the system clipboard, flags it as sensitive on Android 13+,
     * and cancels any previously pending auto-clear timer before scheduling a new one.
     */
    fun copySensitiveText(
        context: Context,
        label: String,
        text: String,
        clearAfterMillis: Long = 30_000L,
        scope: CoroutineScope = defaultScope
    ) {
        val appContext = context.applicationContext
        val clipboard = appContext.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
        val clip = ClipData.newPlainText(label, text).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                description.extras = PersistableBundle().apply {
                    putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                }
            }
        }
        clipboard.setPrimaryClip(clip)

        clearJob?.cancel()
        if (clearAfterMillis > 0L) {
            clearJob = scope.launch {
                delay(clearAfterMillis)
                clearIfMatches(clipboard, text)
            }
        }
    }

    /**
     * Immediately clears the primary clipboard entry.
     */
    fun clearNow(context: Context) {
        clearJob?.cancel()
        clearJob = null
        val clipboard = context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
        clearClipboardInternal(clipboard)
    }

    private fun clearIfMatches(clipboard: ClipboardManager, expectedText: String) {
        val current = runCatching {
            clipboard.primaryClip?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.text?.toString()
        }.getOrNull()
        if (current == expectedText) {
            clearClipboardInternal(clipboard)
        }
    }

    private fun clearClipboardInternal(clipboard: ClipboardManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            clipboard.clearPrimaryClip()
        } else {
            clipboard.setPrimaryClip(ClipData.newPlainText("", ""))
        }
    }
}
