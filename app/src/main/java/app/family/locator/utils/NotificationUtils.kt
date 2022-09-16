package app.family.locator.utils;

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.family.domain.models.Message
import app.family.locator.R
import app.family.locator.ui.activities.MainActivity

class NotificationUtils(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat
) {
    fun getForegroundNotification(): Notification {
        createNotificationChannel(
            FOREGROUND_SERVICE_CHANNEL_ID,
            context.getString(R.string.fg_notif_channel_name),
            context.getString(R.string.fg_notif_channel_desc)

        )

        return buildForegroundNotification()

    }

    fun showChatNotification(messages: List<Message>) {
        createNotificationChannel(
            CHAT_CHANNEL_ID,
            context.getString(R.string.chat_notif_channel_name),
            context.getString(R.string.chat_notif_channel_name)
        )

        notificationManager.notify(CHAT_NOTIFICATION_ID, buildChatNotification(messages))
    }

    fun clearChatNotification() {
        notificationManager.cancel(CHAT_NOTIFICATION_ID)
    }

    private fun buildChatNotification(messages: List<Message>): Notification {
        val title = if (messages.size == 1) {
            messages.first().senderName
        } else {
            "New messages"
        }
        val description = if (messages.size == 1) {
            messages.first().message
        } else {
            "${messages.size} Unread messages"
        }

        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        return NotificationCompat.Builder(context, CHAT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(resultPendingIntent)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun buildForegroundNotification(): Notification {
        return NotificationCompat.Builder(context, FOREGROUND_SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setContentTitle(context.getString(R.string.fg_notif_title))
            .setOngoing(true)
            .setCategory(Notification.CATEGORY_LOCATION_SHARING)
            .setContentText(context.getString(R.string.fg_notif_desc))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel(id: String, name: String, description: String) {
        notificationManager.createNotificationChannel(
            NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW).also {
                it.description = description
            }
        )
    }

    companion object {
        private const val FOREGROUND_SERVICE_CHANNEL_ID = "foreground_service"
        private const val CHAT_CHANNEL_ID = "chat"

        private const val CHAT_NOTIFICATION_ID = 2
    }
}