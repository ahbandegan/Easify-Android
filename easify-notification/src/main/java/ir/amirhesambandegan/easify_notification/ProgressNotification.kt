package ir.amirhesambandegan.easify_notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Helper class for managing and updating progress notifications, supporting determinate and indeterminate states.
 *
 * @param context The application or component [Context].
 * @param channelId The notification channel ID where this notification will be posted.
 * @param notificationId Unique identifier for this notification.
 * @param title The title text of the notification.
 * @param iconRes The drawable resource ID for the small icon.
 */
class ProgressNotification(
    private val context: Context,
    private val channelId: String,
    private val notificationId: Int,
    private val title: String,
    private val iconRes: Int
) {
    private val builder = NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText("Starting...")
        .setSmallIcon(iconRes)
        .setPriority(NotificationCompat.PRIORITY_LOW)

    private val manager = NotificationManagerCompat.from(context)

    /**
     * Updates the notification with determinate progress values and an optional message.
     *
     * @param progress The current progress value.
     * @param max The maximum progress value. Defaults to 100.
     * @param message The text message to display alongside progress. Defaults to "$progress%".
     */
    fun updateProgress(progress: Int, max: Int = 100, message: String = "$progress%") {
        builder.setProgress(max, progress, false)
            .setContentText(message)
        notifyManager()
    }

    /**
     * Sets the notification progress to an indeterminate (continuous spinner) state.
     *
     * @param message The text message to display. Defaults to "Processing...".
     */
    fun setIndeterminate(message: String = "Processing...") {
        builder.setProgress(0, 0, true)
            .setContentText(message)
        notifyManager()
    }

    /**
     * Removes the progress bar indicator and updates the notification to indicate completion.
     *
     * @param successMessage The message indicating task completion. Defaults to "Complete".
     */
    fun finish(successMessage: String = "Complete") {
        builder.setProgress(0, 0, false)
            .setContentText(successMessage)
        notifyManager()
    }
    
    /**
     * Cancels and dismisses this progress notification from the system notification tray.
     */
    fun cancel() {
        manager.cancel(notificationId)
    }

    /**
     * Posts or updates the notification using [NotificationManagerCompat], handling potential security exceptions safely.
     */
    private fun notifyManager() {
        try {
            manager.notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
