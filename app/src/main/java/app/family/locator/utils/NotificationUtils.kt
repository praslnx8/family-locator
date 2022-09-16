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
import androidx.core.app.Person
import app.family.domain.models.Message
import app.family.locator.R
import app.family.locator.ui.activities.MainActivity

class NotificationUtils(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat
) {
    fun getForegroundNotification(): Notification {
        createNotificationChannel(
            id = FOREGROUND_SERVICE_CHANNEL_ID,
            name = context.getString(R.string.fg_notif_channel_name),
            description = context.getString(R.string.fg_notif_channel_desc),
            priority = NotificationManager.IMPORTANCE_LOW
        )

        return buildForegroundNotification()

    }

    fun showChatNotification(messages: List<Message>) {
        createNotificationChannel(
            id = CHAT_CHANNEL_ID,
            name = context.getString(R.string.chat_notif_channel_name),
            description = context.getString(R.string.chat_notif_channel_name),
            priority = NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.notify(CHAT_NOTIFICATION_ID, buildChatNotification(messages))
    }

    fun clearChatNotification() {
        notificationManager.cancel(CHAT_NOTIFICATION_ID)
    }

    private fun buildChatNotification(messages: List<Message>): Notification {

        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val messageStyle = NotificationCompat.MessagingStyle(
            Person.Builder()
                .setName("Family")
                .build()
        ).setConversationTitle("Family").setGroupConversation(true)
        messages.forEach { message ->
            messageStyle.addMessage(
                message.message, message.time, Person.Builder()
                    .setName(message.senderName)
                    .build()
            )
        }

        return NotificationCompat.Builder(context, CHAT_CHANNEL_ID)
            .setStyle(messageStyle)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setContentIntent(resultPendingIntent)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setVisibility(Notification.VISIBILITY_PRIVATE)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
    }

    private fun buildForegroundNotification(): Notification {

        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        return NotificationCompat.Builder(context, FOREGROUND_SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setContentTitle(context.getString(R.string.fg_notif_title))
            .setOngoing(true)
            .setContentIntent(resultPendingIntent)
            .setCategory(Notification.CATEGORY_LOCATION_SHARING)
            .setContentText(context.getString(R.string.fg_notif_desc))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel(
        id: String,
        name: String,
        description: String,
        priority: Int
    ) {
        notificationManager.createNotificationChannel(
            NotificationChannel(id, name, priority).also {
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