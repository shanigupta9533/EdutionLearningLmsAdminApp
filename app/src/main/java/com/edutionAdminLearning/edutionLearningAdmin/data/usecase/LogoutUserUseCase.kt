package com.edutionAdminLearning.edutionLearningAdmin.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.core.result.map
import com.edutionAdminLearning.edutionLearningAdmin.data.mapper.UserDetailsMapper
import com.edutionAdminLearning.edutionLearningAdmin.data.model.UserDetailsData
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetUserLogoutTimeLies = MyResult<UserDetailsData?, CompleteApiError<Error>>

class GetLogoutUserUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
    private val signupMapper: UserDetailsMapper
) : UseCaseSuspend<Unit, GetUserLogoutTimeLies> {
    override suspend fun invoke(params: Unit): GetUserLogoutTimeLies {
        return lmsEdutionDataSource.userLogout().map(signupMapper::dtoToDomain)
    }
}