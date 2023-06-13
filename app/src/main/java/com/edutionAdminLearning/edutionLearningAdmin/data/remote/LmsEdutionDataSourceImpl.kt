package com.edutionAdminLearning.edutionLearningAdmin.data.remote

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.core.result.exeApi
import com.edutionAdminLearning.core.result.getDataResult
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.BannerDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.CourseDetailDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.LoginSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.NotificationDataDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseDetailsDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseDetailsUpdateDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.SignUpSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.UserDetailsResponseDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.VideoDataDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.CoursesVideoDetailsDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.CoursesVideoDto
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

    override suspend fun videoDetails(keywords: String): MyResult<List<VideoDataDto>, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.getVideoDetails(keywords)
        }.getDataResult()
    }

    override suspend fun coursesUpdateLive(courseId: String): MyResult<Unit, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.courseUpdateLive(courseId)
        }.getDataResult()
    }

    override suspend fun coursesDetailsDelete(params: String): MyResult<Unit, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.deleteCourseDetails(params)
        }.getDataResult()
    }

    override suspend fun videoDetailsDelete(videoId: String): MyResult<Unit, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.deleteVideoDelete(videoId)
        }.getDataResult()
    }

    override suspend fun retryVideoUseCase(videoId: String): MyResult<Boolean, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.retryVideo(videoId)
        }.getDataResult()
    }

    override suspend fun getPurchaseDetails(params: String): MyResult<List<PurchaseDetailsDto>, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.getPurchaseDetails(params)
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

    override suspend fun getVideoDetails(courseId: String): MyResult<List<CoursesVideoDto>, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.getCoursesVideoDetails(courseId)
        }.getDataResult()
    }

    override suspend fun videoDeleteDetails(videoId: String): MyResult<Unit, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.courseVideoDelete(videoId)
        }.getDataResult()
    }

    override suspend fun getUserSplash(): MyResult<UserDetailsResponseDto?, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.getUserInSplash()
        }.getDataResult()
    }

    override suspend fun userLogout(): MyResult<UserDetailsResponseDto, CompleteApiError<Error>> {
        return exeApi {
            lmsApi.getLogoutUser()
        }.getDataResult()
    }

}