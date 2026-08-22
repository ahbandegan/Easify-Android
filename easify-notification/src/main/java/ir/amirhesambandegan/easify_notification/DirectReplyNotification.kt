package ir.amirhesambandegan.easify_notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput

/**
 * Utility object for displaying notifications with inline direct reply capability using [RemoteInput].
 */
object DirectReplyNotification {

    /**
     * Intent extra key used to retrieve the inline reply text from the user's input.
     */
    const val KEY_TEXT_REPLY = "key_text_reply"

    /**
     * Displays a notification that allows the user to directly type and send a reply.
     *
     * @param context The application or component [Context].
     * @param channelId The notification channel ID where this notification will be posted.
     * @param notificationId Unique identifier for this notification.
     * @param title The title text of the notification.
     * @param message The body text of the notification.
     * @param iconRes The drawable resource ID for the small icon and action icon.
     * @param replyActionTitle The label for the reply action button and input field. Defaults to "Reply".
     * @param intentClass The class of the [android.content.BroadcastReceiver] or component intended to receive the reply.
     */
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
