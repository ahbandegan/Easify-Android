package ir.amirhesambandegan.easify_notification

import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Utility object for displaying rich notifications using expandable styles like BigText and BigPicture.
 */
object BigStyleNotification {

    /**
     * Displays an expandable big text notification.
     *
     * @param context The application or component [Context].
     * @param channelId The notification channel ID where this notification will be posted.
     * @param notificationId Unique identifier for this notification.
     * @param title The title text of the notification.
     * @param shortMessage The summary text shown in collapsed state.
     * @param longMessage The full expanded text displayed when the notification is expanded.
     * @param iconRes The drawable resource ID for the small icon.
     */
    fun showBigText(
        context: Context,
        channelId: String,
        notificationId: Int,
        title: String,
        shortMessage: String,
        longMessage: String,
        iconRes: Int
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setContentText(shortMessage)
            .setStyle(NotificationCompat.BigTextStyle().bigText(longMessage))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notify(context, notificationId, builder)
    }

    /**
     * Displays a notification with an expandable large picture.
     *
     * @param context The application or component [Context].
     * @param channelId The notification channel ID where this notification will be posted.
     * @param notificationId Unique identifier for this notification.
     * @param title The title text of the notification.
     * @param message The body text of the notification.
     * @param bitmap The [Bitmap] image to be displayed in expanded view and as a large icon.
     * @param iconRes The drawable resource ID for the small icon.
     */
    fun showBigPicture(
        context: Context,
        channelId: String,
        notificationId: Int,
        title: String,
        message: String,
        bitmap: Bitmap,
        iconRes: Int
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null as Bitmap?) // Prevents showing double icons on some devices
            )
            .setLargeIcon(bitmap)

        notify(context, notificationId, builder)
    }

    /**
     * Helper method to post the notification safely handling potential security exceptions.
     *
     * @param context The application or component [Context].
     * @param id Unique identifier for the notification.
     * @param builder The configured [NotificationCompat.Builder] instance.
     */
    private fun notify(context: Context, id: Int, builder: NotificationCompat.Builder) {
        try {
            NotificationManagerCompat.from(context).notify(id, builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
