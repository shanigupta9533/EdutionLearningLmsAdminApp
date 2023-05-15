package com.edutionLearning.edutionlearningapp.di

import com.edutionLearning.core.network.PWService
import com.edutionLearning.edutionlearningapp.data.remote.LmsApi
import com.edutionLearning.edutionlearningapp.data.remote.LmsEdutionDataSource
import com.edutionLearning.edutionlearningapp.data.remote.LmsEdutionDataSourceImpl
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