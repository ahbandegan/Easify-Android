package ir.amirhesambandegan.easify_context

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

private fun Context.addNewTaskIfNeeded(intent: Intent) {
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}

/**
 * Opens the system dialer with [phoneNumber], preserving `#` and `*` via `Uri.fromParts`.
 */
fun Context.makeCall(phoneNumber: String) {
    val sanitized = phoneNumber.trim()
    if (sanitized.isEmpty()) return
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.fromParts("tel", sanitized, null)
        addNewTaskIfNeeded(this)
    }
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
    }
}

/**
 * Opens the system email client safely.
 */
fun Context.sendEmail(to: String, subject: String = "", body: String = "") {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
        addNewTaskIfNeeded(this)
    }
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
    }
}

/**
 * Opens an `http://` or `https://` URL in the system browser, rejecting unsafe non-web URI schemes.
 */
fun Context.openBrowser(url: String) {
    val parsed = runCatching { Uri.parse(url.trim()) }.getOrNull() ?: return
    val scheme = parsed.scheme?.lowercase()
    if (scheme != "http" && scheme != "https") return

    val intent = Intent(Intent.ACTION_VIEW, parsed).apply {
        addNewTaskIfNeeded(this)
    }
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
    }
}

/**
 * Opens the system share sheet with [text].
 */
fun Context.shareText(text: String, title: String = "Share via") {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, title).apply {
        addNewTaskIfNeeded(this)
    }
    try {
        startActivity(shareIntent)
    } catch (_: ActivityNotFoundException) {
    }
}

/**
 * Opens geographic coordinates on a map application safely.
 */
fun Context.openMap(lat: Double, lng: Double, label: String = "Location") {
    val encodedLabel = Uri.encode(label)
    val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng($encodedLabel)")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        addNewTaskIfNeeded(this)
    }
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
    }
}
