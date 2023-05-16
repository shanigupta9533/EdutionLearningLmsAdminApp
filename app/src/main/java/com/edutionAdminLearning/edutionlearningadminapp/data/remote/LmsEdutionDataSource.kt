package com.edutionAdminLearning.edutionlearningadminapp.data.remote

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.CourseDetailDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.LoginSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.PurchaseDetailsDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.PurchaseSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.SignUpSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.UserDetailsResponseDto

interface LmsEdutionDataSource {

    suspend fun getSignupResponse(submitDto: SignUpSubmitDto): MyResult<UserDetailsResponseDto?, CompleteApiError<Error>>
    suspend fun getLoginResponse(loginSubmitDto: LoginSubmitDto): MyResult<UserDetailsResponseDto?, CompleteApiError<Error>>
    suspend fun coursesDetails(): MyResult<List<CourseDetailDto>, CompleteApiError<Error>>
    suspend fun coursesDetailsDelete(params: String): MyResult<Unit, CompleteApiError<Error>>
    suspend fun getPurchaseDetails(): MyResult<List<PurchaseDetailsDto>, CompleteApiError<Error>>
    suspend fun getPurchaseSubmit(purchaseSubmitDto: PurchaseSubmitDto): MyResult<Unit, CompleteApiError<Error>>

}