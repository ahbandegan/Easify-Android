package ir.amirhesambandegan.easify_notification

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Utility object for displaying notifications with interactive action buttons.
 */
object ActionButtonsNotification {

    /**
     * Represents an action button configuration for a notification.
     *
     * @property iconRes The drawable resource ID for the action icon.
     * @property title The label/text displayed on the action button.
     * @property pendingIntent The [PendingIntent] to execute when the action button is clicked.
     */
    data class NotificationAction(
        val iconRes: Int,
        val title: String,
        val pendingIntent: PendingIntent
    )

    /**
     * Displays a notification with the specified action buttons.
     *
     * @param context The application or component [Context].
     * @param channelId The notification channel ID where this notification will be posted.
     * @param notificationId Unique identifier for this notification.
     * @param title The title text of the notification.
     * @param message The body text of the notification.
     * @param iconRes The drawable resource ID for the small icon.
     * @param actions A list of [NotificationAction] instances representing the action buttons to display.
     */
    fun show(
        context: Context,
        channelId: String,
        notificationId: Int,
        title: String,
        message: String,
        iconRes: Int,
        actions: List<NotificationAction>
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        actions.forEach { action ->
            builder.addAction(action.iconRes, action.title, action.pendingIntent)
        }

        try {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
