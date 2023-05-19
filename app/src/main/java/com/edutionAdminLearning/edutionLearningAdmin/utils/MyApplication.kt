package com.edutionAdminLearning.edutionLearningAdmin.utils

import android.app.Application
import androidx.startup.AppInitializer
import com.edutionAdminLearning.edutionLearningAdmin.workManager.CustomWorkManagerInitializer
import com.mocklets.pluto.Pluto
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Pluto.initialize(applicationContext)
        AppInitializer.getInstance(this).initializeComponent(CustomWorkManagerInitializer::class.java)

    }

}