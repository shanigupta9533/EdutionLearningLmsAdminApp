package com.edutionLearning.edutionlearningapp.WorkManager

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.edutionLearning.core.result.onFailure
import com.edutionLearning.core.result.onSuccess
import com.edutionLearning.core.type.value
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val downloadTitle = "Updating video player data"

const val COURSE_ID = "course_id"
const val VIDEO_ID = "video_id"
const val TIMESTAMP = "timestamp"

@HiltWorker
class WorkQuene @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted parameters: WorkerParameters,
) : CoroutineWorker(appContext, parameters) {

    private val notificationId = (1..50000).shuffled().last()
    private val courseId = inputData.getString(COURSE_ID)
    private val videoId = inputData.getString(VIDEO_ID)
    private val timestamp = inputData.getString(TIMESTAMP)

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    override suspend fun doWork(): Result {

        setForegroundAsync(getForegroundInfo())

        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return showNotification()
    }

    private fun showNotification(): ForegroundInfo {
        return ForegroundInfo(notificationId, createForegroundInfo().build())
    }

    private fun createForegroundInfo(): NotificationCompat.Builder {
        val id = "$notificationId"
        val title = downloadTitle

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(id, title)
        }

        return NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText("Please wait a couple of seconds")
            .setSmallIcon(com.edutionLearning.core_ui.R.drawable.ic_play_new)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        name: String
    ): NotificationChannel {
        return NotificationChannel(
            channelId, name, NotificationManager.IMPORTANCE_LOW
        ).also { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }
}