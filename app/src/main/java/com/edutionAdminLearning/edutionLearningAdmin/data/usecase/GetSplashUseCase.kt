package com.edutionAdminLearning.edutionLearningAdmin.data.usecase

import com.edutionAdminLearning.core.result.map
import com.edutionAdminLearning.edutionLearningAdmin.data.mapper.UserDetailsMapper
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

class GetSplashUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
    private val userDetailsMapper: UserDetailsMapper
): UseCaseSuspend<Unit, GetSignupResponseTimeLies> {
    override suspend fun invoke(params: Unit): GetSignupResponseTimeLies {
        return lmsEdutionDataSource.getUserSplash().map(userDetailsMapper::dtoToDomain)
    }
}