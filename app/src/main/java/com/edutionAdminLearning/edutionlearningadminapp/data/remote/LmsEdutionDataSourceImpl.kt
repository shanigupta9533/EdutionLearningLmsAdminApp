package com.edutionAdminLearning.edutionlearningadminapp.data.remote

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.core.result.exeApi
import com.edutionAdminLearning.core.result.getDataResult
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.CourseDetailDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.LoginSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.PurchaseDetailsDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.PurchaseSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.SignUpSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.UserDetailsResponseDto
import javax.inject.Inject

class LmsEdutionDataSourceImpl @Inject constructor(
    private val lmsApi: LmsApi
) : LmsEdutionDataSource {

    override suspend fun getSignupResponse(submitDto: SignUpSubmitDto): MyResult<UserDetailsResponseDto?, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.signupUser(submitDto)
        }.getDataResult()
    }

    override suspend fun getLoginResponse(loginSubmitDto: LoginSubmitDto): MyResult<UserDetailsResponseDto?, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.loginUser(loginSubmitDto)
        }.getDataResult()
    }

    override suspend fun coursesDetails(): MyResult<List<CourseDetailDto>, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.getAllCoursesDetails()
        }.getDataResult()
    }

    override suspend fun coursesDetailsDelete(params: String): MyResult<Unit, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.deleteCourseDetails(params)
        }.getDataResult()
    }

    override suspend fun getPurchaseDetails(): MyResult<List<PurchaseDetailsDto>, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.getPurchaseDetails()
        }.getDataResult()
    }

    override suspend fun getPurchaseSubmit(purchaseSubmitDto: PurchaseSubmitDto): MyResult<Unit, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.purchaseInsert(purchaseSubmitDto)
        }.getDataResult()
    }

}