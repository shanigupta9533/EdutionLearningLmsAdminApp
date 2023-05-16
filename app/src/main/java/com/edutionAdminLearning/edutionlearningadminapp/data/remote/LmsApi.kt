package com.edutionAdminLearning.edutionlearningadminapp.data.remote

import com.edutionAdminLearning.core.result.ApiResponse
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.CourseDetailDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.LoginSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.PurchaseDetailsDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.PurchaseSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.SignUpSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.UserDetailsResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
//
//    @GET("api/getBanner")
//    suspend fun getBanners(
//        @Query("keywords") keywords: String
//    ): Response<ApiResponse<List<BannerDto>>>
//
//    @GET("api/getCoursesVideos")
//    suspend fun getVideoDetails(
//        @Query("course_id") courseId: String
//    ): Response<ApiResponse<VideoDetailsDto>>
//
//    @GET("api/getPurchaseSpec")
//    suspend fun getPurchaseSpec(
//        @Query("course_id") courseId: String
//    ): Response<ApiResponse<List<PurchaseDataDto>>>
//
//    @POST("api/splash")
//    suspend fun getUserInSplash(): Response<ApiResponse<UserDetailsResponseDto>>
//
//    @POST("api/videoTime")
//    suspend fun getVideoTime(
//        @Body videoTimeRequestDto: VideoTimeRequestDto
//    ): Response<ApiResponse<Unit>>
//
//    @POST("api/logout")
//    suspend fun getLogoutUser(
//    ): Response<ApiResponse<UserDetailsResponseDto>>
//
//    @GET("api/notification")
//    suspend fun getNotification(
//    ): Response<ApiResponse<List<NotificationDataDto>>>
//
//    @FormUrlEncoded
//    @POST("api/userSeenNotice")
//    suspend fun setNotification(
//        @Field("user_seen_notice[]") userSeenNotice: List<String>
//    ): Response<ApiResponse<Unit>>
//
//    @GET("api/support")
//    suspend fun setSupport(): Response<ApiResponse<ContactSupportDto>>

}