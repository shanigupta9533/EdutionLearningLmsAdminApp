package com.edutionAdminLearning.edutionLearningAdmin.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.startup.AppInitializer
import com.edutionAdminLearning.edutionLearningAdmin.workManager.CustomWorkManagerInitializer
import com.edutionAdminLearning.network.BuildConfig
import com.mocklets.pluto.Pluto
import dagger.hilt.android.HiltAndroidApp
import net.gotev.uploadservice.UploadServiceConfig

@HiltAndroidApp
class MyApplication : Application() {

    var Channel = Constants.NOTIFICATION_CHANNEL_ID
    override fun onCreate() {
        super.onCreate()
        Pluto.initialize(applicationContext)
        AppInitializer.getInstance(this).initializeComponent(CustomWorkManagerInitializer::class.java)

        UploadServiceConfig.initialize(
            this, Channel,
            BuildConfig.DEBUG
        ) // TODO: NTR

        createNotificationChannel()

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                Channel, Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW
            )
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel)
        }
    }

}