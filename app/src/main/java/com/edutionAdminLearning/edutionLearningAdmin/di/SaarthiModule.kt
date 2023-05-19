package com.edutionAdminLearning.edutionLearningAdmin.di

import com.edutionAdminLearning.core.network.PWService
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsApi
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SaarthiModule {

    companion object {
        @Singleton
        @Provides
        fun bindSarthiAPI(pwService: PWService): LmsApi {
            return pwService.create(LmsApi::class.java)
        }
    }

    @Binds
    abstract fun bindSarthiDataSource(
        lmsEdutionDataSourceImpl: LmsEdutionDataSourceImpl
    ): LmsEdutionDataSource

}