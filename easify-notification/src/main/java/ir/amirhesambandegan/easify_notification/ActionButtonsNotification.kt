package ir.amirhesambandegan.easify_notification

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object ActionButtonsNotification {

    data class NotificationAction(
        val iconRes: Int,
        val title: String,
        val pendingIntent: PendingIntent
    )

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
