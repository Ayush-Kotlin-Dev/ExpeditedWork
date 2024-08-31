package com.ayush.ExpeditedWork

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class CustomWorker constructor(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        delay(20000)
        Log.d("CustomWorker", "Work completed")
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createForegroundInfo(applicationContext)
    }
}

private fun createForegroundInfo(context: Context): ForegroundInfo {
    val notification = createNotification(context)
    return ForegroundInfo(1, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
}

private fun createNotification(context: Context): Notification {
    val channel1Id = "channelId"
    val channel1Name = "Channel 1"

    val builder = NotificationCompat.Builder(context, channel1Id)
        .setContentTitle("Notification Title")
        .setContentText("This is my First Notification")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setColor(ContextCompat.getColor(context, R.color.purple_500))
        .setOngoing(true)
        .setAutoCancel(true)

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel1 = NotificationChannel(
            channel1Id,
            channel1Name,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel1)
    }
    return builder.build()
}

