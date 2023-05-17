package com.edutionAdminLearning.edutionlearningadminapp.data.remote

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.core.result.exeApi
import com.edutionAdminLearning.core.result.getDataResult
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.BannerDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.CourseDetailDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.LoginSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.NotificationDataDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.PurchaseDetailsDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.PurchaseDetailsUpdateDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.PurchaseSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.SignUpSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.UserDetailsResponseDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.VideoDetailsDto
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

    override suspend fun getPurchaseUpdate(purchaseId: String,purchaseDetailsUpdateDto: PurchaseDetailsUpdateDto): MyResult<Unit, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.purchaseDetailsUpdate(
                purchaseId = purchaseId,
               purchaseDetailsUpdateDto =  purchaseDetailsUpdateDto
            )
        }.getDataResult()
    }

    override suspend fun purchaseDelete(purchaseId: String): MyResult<Unit, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.purchaseDetailsDelete(purchaseId)
        }.getDataResult()
    }

    override suspend fun getAllBanners(keywords: String): MyResult<List<BannerDto>, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.getBanners(keywords)
        }.getDataResult()
    }

    override suspend fun deleteBanner(bannerId: String): MyResult<Unit, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.deleteBanner(bannerId)
        }.getDataResult()
    }

    override suspend fun getNotification(): MyResult<List<NotificationDataDto>, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.getNotification()
        }.getDataResult()
    }

    override suspend fun notificationInsert(notification: String): MyResult<Unit, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.notificationInsert(notification)
        }.getDataResult()
    }

    override suspend fun notificationDelete(notificationId: String): MyResult<Unit, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.notificationDelete(notificationId)
        }.getDataResult()
    }

    override suspend fun getVideoDetails(courseId: String): MyResult<VideoDetailsDto?, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.getVideoDetails(courseId)
        }.getDataResult()
    }

    override suspend fun videoDeleteDetails(videoId: String): MyResult<Unit, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.courseVideoDelete(videoId)
        }.getDataResult()
    }

}