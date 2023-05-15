package com.edutionLearning.edutionlearningapp.data.remote

import com.edutionLearning.core.result.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface LmsApi {

//    @POST("api/signup")
//    suspend fun signupUser(
//        @Body body: SignUpSubmitDto
//    ): Response<ApiResponse<UserDetailsResponseDto>>
//
//    @POST("api/login")
//    suspend fun loginUser(
//        @Body body: LoginSubmitDto
//    ): Response<ApiResponse<UserDetailsResponseDto>>

//    @FormUrlEncoded
//    @POST("api/otp")
//    suspend fun getOtp(
//        @Field("mobile") mobile: String
//    ): Response<ApiResponse<Unit>>
//
//    @POST("api/compareOtp")
//    suspend fun submitOtpVerified(
//        @Body otpVerifySubmitDto: OtpVerifySubmitDto
//    ): Response<ApiResponse<UserDetailsResponseDto>>
//
//    @POST("api/changePassword")
//    suspend fun changePassword(
//        @Body changePasswordRequestDto: ChangePasswordRequestDto
//    ): Response<ApiResponse<UserDetailsResponseDto>>
//
//    @GET("api/courseDetails")
//    suspend fun allCourseDetails(
//        @Query("keywords") keywords: String
//    ): Response<ApiResponse<List<CourseDetailDto>>>
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