package ir.amirhesambandegan.easify_notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Utility object for displaying grouped notifications and summary notifications.
 */
object GroupedNotification {

    /**
     * Displays a notification that belongs to a specific notification group or acts as a group summary.
     *
     * @param context The application or component [Context].
     * @param channelId The notification channel ID where this notification will be posted.
     * @param notificationId Unique identifier for this notification.
     * @param groupId Key identifier used to cluster related notifications together into a group.
     * @param title The title text of the notification.
     * @param message The body text of the notification.
     * @param iconRes The drawable resource ID for the small icon.
     * @param isGroupSummary Whether this notification should act as the group summary notification. Defaults to false.
     * @param summaryText Additional summary text displayed when this notification is configured as a group summary. Defaults to empty string.
     */
    fun show(
        context: Context,
        channelId: String,
        notificationId: Int,
        groupId: String,
        title: String,
        message: String,
        iconRes: Int,
        isGroupSummary: Boolean = false,
        summaryText: String = ""
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setContentText(message)
            .setGroup(groupId)
            
        if (isGroupSummary) {
            builder.setGroupSummary(true)
                .setStyle(NotificationCompat.InboxStyle().setSummaryText(summaryText))
        }

        val manager = NotificationManagerCompat.from(context)
        try {
            manager.notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
