package com.github.engineeredtoimperfection.breathe.common

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity.NOTIFICATION_SERVICE
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.github.engineeredtoimperfection.breathe.MainActivity
import com.github.engineeredtoimperfection.breathe.R
import com.github.engineeredtoimperfection.breathe.data.markGentleNudgeAsEnabled
import java.util.Calendar

const val INTENT_ACTION_GENTLE_NUDGE = "com.github.engineeredtoimperfection.breathe.ACTION_GENTLE_NUDGE"

data class NotificationChannelSettings(
    val channelID: String,
    val channelName: String,
    val description: String,
    val importance: Int
) {

    fun toNotificationChannel(): NotificationChannel {
        return NotificationChannel(channelID, channelName, importance).apply {
            description = description
        }
    }
}

val remindersNotificationChannelSettings = NotificationChannelSettings(
    channelID = "Reminders",
    channelName = "Gentle Reminders",
    description = "Gentle nudges to keep you calm and relaxed.",
    importance = NotificationManager.IMPORTANCE_DEFAULT
)

fun Context.notificationManager(): NotificationManager {
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager
}

fun createRemindersNotificationChannel(activity: Activity) {
    createNotificationChannel(activity, remindersNotificationChannelSettings)
}

fun createNotificationChannel(
    activity: Activity,
    notificationChannelSettings: NotificationChannelSettings
) {

    val channel = notificationChannelSettings.toNotificationChannel()

    // Register the channel with the system. You can't change the importance
    // or other notification behaviors after this.
    activity.notificationManager().createNotificationChannel(channel)
}

fun sendReminderNotification(context: Context) {
    val remindersChannelId = remindersNotificationChannelSettings.channelID

    val reminderNotification = context.createNotification(channelId = remindersChannelId)
    context.sendNotification(notification = reminderNotification, notificationId = 0)
}

fun Context.createNotification(
    channelId: String,
    smallIcon: Int = R.drawable.ic_launcher_new_foreground,
    contentTitle: String = "Gentle Reminder",
    contentText: String = "Have you taken a moment to breathe today?"
): Notification {
    val intent = Intent(this, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val notification = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(smallIcon)
        .setContentTitle(contentTitle)
        .setContentText(contentText)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    return notification.build()
}

fun Context.sendNotification(notification: Notification, notificationId: Int) {
    notificationManager().notify(
        notificationId,
        notification
    )
}

fun Activity.sendNotification(notification: Notification, notificationId: Int) {

    requestPermissionIfNotGranted(
        doIfGranted = {
            notificationManager().notify(
                notificationId,
                notification
            )
        }
    )

}

suspend fun Activity.scheduleNotificationIfGranted() {
    if (isPermissionGranted()) {
        scheduleNotification()
    }
}

private suspend fun Activity.scheduleNotification() {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
        intent.action = INTENT_ACTION_GENTLE_NUDGE
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 8)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    alarmManager.setInexactRepeating(
        AlarmManager.RTC,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY,
        alarmIntent
    )

    Toast.makeText(this, "Scheduled Notifications", Toast.LENGTH_SHORT).show()

    markGentleNudgeAsEnabled()
}

fun Activity.requestPermissionIfNotGranted(doIfGranted: () -> Unit = {} ) {
    if (!isPermissionGranted()) {
        requestPermissionFromUser()
    } else {
        doIfGranted()
    }
}

private fun Activity.isPermissionGranted() = ActivityCompat.checkSelfPermission(
    this,
    Manifest.permission.POST_NOTIFICATIONS
) == PackageManager.PERMISSION_GRANTED

private fun Activity.requestPermissionFromUser() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
        0
    )
}
