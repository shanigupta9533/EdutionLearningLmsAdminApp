package com.edutionLearning.edutionlearningapp.utils

import android.app.Application
import androidx.startup.AppInitializer
import com.edutionLearning.edutionlearningapp.WorkManager.CustomWorkManagerInitializer
import com.edutionLearning.network.BuildConfig
import com.mocklets.pluto.Pluto
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Pluto.initialize(applicationContext)
        AppInitializer.getInstance(this).initializeComponent(CustomWorkManagerInitializer::class.java)

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(BuildConfig.ONE_SIGNAL_APP_ID)

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();

    }

}