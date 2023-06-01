package com.edutionAdminLearning.edutionLearningAdmin.di

import android.content.Context
import com.edutionAdminLearning.core.network.PWService
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.network.BuildConfig
import com.edutionAdminLearning.network.utils.FileUploaderService
import com.edutionAdminLearning.network.utils.LoginManager
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

    @Singleton
    @Provides
    fun provideFileUploaderService(
        @ApplicationContext context: Context,
        loginManager: LoginManager
    ) = FileUploaderService(context, BuildConfig.BASE_URL) {
            return@FileUploaderService loginManager.getToken().value
        }

}