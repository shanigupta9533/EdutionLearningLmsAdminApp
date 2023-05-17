package com.edutionAdminLearning.edutionlearningadminapp.data.remote

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
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

interface LmsEdutionDataSource {

    suspend fun getSignupResponse(submitDto: SignUpSubmitDto): MyResult<UserDetailsResponseDto?, CompleteApiError<Error>>
    suspend fun getLoginResponse(loginSubmitDto: LoginSubmitDto): MyResult<UserDetailsResponseDto?, CompleteApiError<Error>>
    suspend fun coursesDetails(): MyResult<List<CourseDetailDto>, CompleteApiError<Error>>
    suspend fun coursesDetailsDelete(params: String): MyResult<Unit, CompleteApiError<Error>>
    suspend fun getPurchaseDetails(): MyResult<List<PurchaseDetailsDto>, CompleteApiError<Error>>
    suspend fun getPurchaseSubmit(purchaseSubmitDto: PurchaseSubmitDto): MyResult<Unit, CompleteApiError<Error>>
    suspend fun getPurchaseUpdate(purchaseId: String, purchaseDetailsUpdateDto: PurchaseDetailsUpdateDto): MyResult<Unit, CompleteApiError<Error>>
    suspend fun purchaseDelete(purchaseId: String): MyResult<Unit, CompleteApiError<Error>>
    suspend fun getAllBanners(keywords: String): MyResult<List<BannerDto>, CompleteApiError<Error>>
    suspend fun deleteBanner(bannerId: String): MyResult<Unit, CompleteApiError<Error>>
    suspend fun getNotification(): MyResult<List<NotificationDataDto>, CompleteApiError<Error>>
    suspend fun notificationInsert(notification: String): MyResult<Unit, CompleteApiError<Error>>
    suspend fun notificationDelete(notificationId: String): MyResult<Unit, CompleteApiError<Error>>
    suspend fun getVideoDetails(courseId: String): MyResult<VideoDetailsDto?, CompleteApiError<Error>>
    suspend fun videoDeleteDetails(videoId: String): MyResult<Unit, CompleteApiError<Error>>

}