package ir.amirhesambandegan.easify_notification

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build

/**
 * Utility object for creating and managing Android Notification Channels and Channel Groups.
 */
object ChannelManager {

    /**
     * Creates a notification channel group on Android 8.0 (API level 26) and above.
     *
     * @param context The application or component [Context].
     * @param groupId Unique identifier for the notification channel group.
     * @param groupName Human-readable name of the group displayed in system settings.
     */
    fun createGroup(context: Context, groupId: String, groupName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannelGroup(NotificationChannelGroup(groupId, groupName))
        }
    }

    /**
     * Creates a notification channel with customizable properties on Android 8.0 (API level 26) and above.
     *
     * @param context The application or component [Context].
     * @param channelId Unique identifier for the notification channel.
     * @param channelName Human-readable name of the channel displayed in system settings.
     * @param importance The importance level of the channel (e.g., [NotificationManager.IMPORTANCE_DEFAULT]).
     * @param description Optional user-visible description of what notifications in this channel represent.
     * @param groupId Optional ID of a [NotificationChannelGroup] this channel belongs to.
     * @param soundUri Optional [Uri] pointing to a custom notification sound.
     * @param enableLights Whether notification lights are enabled for this channel.
     * @param lightColor The color of the notification light if enabled.
     * @param enableVibration Whether vibration is enabled for this channel.
     * @param vibrationPattern The vibration pattern to use when a notification is posted.
     */
    fun createChannel(
        context: Context,
        channelId: String,
        channelName: String,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        description: String? = null,
        groupId: String? = null,
        soundUri: Uri? = null,
        enableLights: Boolean = true,
        lightColor: Int = Color.BLUE,
        enableVibration: Boolean = true,
        vibrationPattern: LongArray = longArrayOf(0, 250, 250, 250)
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                this.description = description
                this.group = groupId
                this.enableLights(enableLights)
                this.lightColor = lightColor
                this.enableVibration(enableVibration)
                this.vibrationPattern = vibrationPattern
                
                if (soundUri != null) {
                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                    this.setSound(soundUri, audioAttributes)
                } else if (importance < NotificationManager.IMPORTANCE_DEFAULT) {
                    this.setSound(null, null)
                }
            }
            
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
