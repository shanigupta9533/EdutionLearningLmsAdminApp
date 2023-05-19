package com.edutionAdminLearning.edutionLearningAdmin.data.remote

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.BannerDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.CourseDetailDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.LoginSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.NotificationDataDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseDetailsDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseDetailsUpdateDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.SignUpSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.UserDetailsResponseDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.VideoDetailsDto

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