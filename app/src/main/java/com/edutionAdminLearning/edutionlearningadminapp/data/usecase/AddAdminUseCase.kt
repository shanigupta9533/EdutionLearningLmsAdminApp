package com.edutionAdminLearning.edutionlearningadminapp.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.core.result.map
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.SignUpSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.mapper.UserDetailsMapper
import com.edutionAdminLearning.edutionlearningadminapp.data.model.UserDetailsData
import com.edutionAdminLearning.edutionlearningadminapp.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetSignupResponseTimeLies = MyResult<UserDetailsData?, CompleteApiError<Error>>

class AddAdminUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
    private val signupMapper: UserDetailsMapper
) : UseCaseSuspend<SignUpSubmitDto, GetSignupResponseTimeLies> {
    override suspend fun invoke(params: SignUpSubmitDto): GetSignupResponseTimeLies {
       return lmsEdutionDataSource.getSignupResponse(params).map(signupMapper::dtoToDomain)
    }

}