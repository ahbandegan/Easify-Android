package ir.amirhesambandegan.easify_notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object GroupedNotification {

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
