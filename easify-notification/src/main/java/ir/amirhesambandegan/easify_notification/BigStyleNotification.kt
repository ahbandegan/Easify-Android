package ir.amirhesambandegan.easify_notification

import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object BigStyleNotification {

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

    private fun notify(context: Context, id: Int, builder: NotificationCompat.Builder) {
        try {
            NotificationManagerCompat.from(context).notify(id, builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
