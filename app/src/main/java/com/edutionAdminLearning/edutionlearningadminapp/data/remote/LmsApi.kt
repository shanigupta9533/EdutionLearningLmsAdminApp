package com.edutionAdminLearning.edutionlearningadminapp.data.remote

import com.edutionAdminLearning.core.result.ApiResponse
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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LmsApi {

    @POST("signup")
    suspend fun signupUser(
        @Body body: SignUpSubmitDto
    ): Response<ApiResponse<UserDetailsResponseDto>>

    @POST("login")
    suspend fun loginUser(
        @Body body: LoginSubmitDto
    ): Response<ApiResponse<UserDetailsResponseDto>>

    @FormUrlEncoded
    @GET("allCourses")
    suspend fun getAllCoursesDetails(
    ): Response<ApiResponse<List<CourseDetailDto>>>

    @DELETE("courseDetails/{courseId}")
    suspend fun deleteCourseDetails(
        @Path("courseId") courseId: String,
        ): Response<ApiResponse<Unit>>

    @GET("purchaseDetails")
    suspend fun getPurchaseDetails(
    ): Response<ApiResponse<List<PurchaseDetailsDto>>>

    @POST("purchaseDetails")
    suspend fun purchaseInsert(
        @Body purchaseSubmitDto: PurchaseSubmitDto
    ): Response<ApiResponse<Unit>>

    @POST("purchaseSpec/{purchaseId}")
    suspend fun purchaseDetailsUpdate(
        @Path("purchaseId") purchaseId: String,
        @Body purchaseDetailsUpdateDto: PurchaseDetailsUpdateDto
    ): Response<ApiResponse<Unit>>

    @DELETE("purchaseSpec/{purchaseId}")
    suspend fun purchaseDetailsDelete(
        @Path("purchaseId") purchaseId: String,
    ): Response<ApiResponse<Unit>>

    @GET("banner")
    suspend fun getBanners(
        @Query("keywords") keywords: String
    ): Response<ApiResponse<List<BannerDto>>>

    @DELETE("banner/{banner_id}")
    suspend fun deleteBanner(
        @Path("banner_id") bannerId: String
    ): Response<ApiResponse<Unit>>

    @GET("notification")
    suspend fun getNotification(
    ): Response<ApiResponse<List<NotificationDataDto>>>

//    @POST("api/splash")
//    suspend fun getUserInSplash(): Response<ApiResponse<UserDetailsResponseDto>>

    @FormUrlEncoded
    @POST("notification")
    suspend fun notificationInsert(
        @Field("notice_text") notificationText: String
    ): Response<ApiResponse<Unit>>

    @DELETE("notification/{notificationId}")
    suspend fun notificationDelete(
        @Path("notificationId") notificationId: String
    ): Response<ApiResponse<Unit>>

    @GET("courseVideo")
    suspend fun getVideoDetails(
        @Query("course_id") courseId: String
    ): Response<ApiResponse<VideoDetailsDto>>

    @DELETE("courseVideo/{video_id}")
    suspend fun courseVideoDelete(
        @Path("video_id") videoId: String
    ): Response<ApiResponse<Unit>>


}