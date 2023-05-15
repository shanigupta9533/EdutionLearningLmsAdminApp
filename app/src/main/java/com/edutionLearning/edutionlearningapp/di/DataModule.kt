package com.edutionLearning.edutionlearningapp.di

import android.content.Context
import com.edutionLearning.core.network.PWService
import com.edutionLearning.core.type.value
import com.edutionLearning.network.BuildConfig
import com.edutionLearning.network.utils.LoginManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideRemoteService(
        @ApplicationContext context: Context,
        loginManager: LoginManager
    ): PWService =
        PWService(
            context,
            BuildConfig.BASE_URL,
        ) { loginManager.getToken().value }

}