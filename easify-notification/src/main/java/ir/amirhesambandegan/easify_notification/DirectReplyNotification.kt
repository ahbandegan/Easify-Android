package ir.amirhesambandegan.easify_notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput

object DirectReplyNotification {

    const val KEY_TEXT_REPLY = "key_text_reply"

    fun show(
        context: Context,
        channelId: String,
        notificationId: Int,
        title: String,
        message: String,
        iconRes: Int,
        replyActionTitle: String = "Reply",
        intentClass: Class<*> // The BroadcastReceiver to handle the reply
    ) {
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel(replyActionTitle)
            .build()

        val replyIntent = Intent(context, intentClass)
        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val action = NotificationCompat.Action.Builder(
            iconRes,
            replyActionTitle,
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setContentText(message)
            .addAction(action)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val manager = NotificationManagerCompat.from(context)
        try {
            manager.notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
