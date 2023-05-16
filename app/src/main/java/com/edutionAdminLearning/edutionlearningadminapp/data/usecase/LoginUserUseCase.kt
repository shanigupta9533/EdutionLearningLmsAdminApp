package com.edutionAdminLearning.edutionlearningadminapp.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.core.result.map
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.LoginSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.mapper.UserDetailsMapper
import com.edutionAdminLearning.edutionlearningadminapp.data.model.UserDetailsData
import com.edutionAdminLearning.edutionlearningadminapp.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetLoginResponseTimeLies = MyResult<UserDetailsData?, CompleteApiError<Error>>

class LoginUserUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
    private val userDetailsMapper: UserDetailsMapper
): UseCaseSuspend<LoginSubmitDto, GetLoginResponseTimeLies> {
    override suspend fun invoke(params: LoginSubmitDto): GetLoginResponseTimeLies {
        return lmsEdutionDataSource.getLoginResponse(params).map(userDetailsMapper::dtoToDomain)
    }
}