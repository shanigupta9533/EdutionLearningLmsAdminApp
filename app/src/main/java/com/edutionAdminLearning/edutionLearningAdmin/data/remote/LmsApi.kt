package com.edutionAdminLearning.edutionLearningAdmin.data.remote

import com.edutionAdminLearning.core.result.ApiResponse
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

    @POST("api/admin/signup")
    suspend fun signupUser(
        @Body body: SignUpSubmitDto
    ): Response<ApiResponse<UserDetailsResponseDto>>

    @POST("api/admin/login")
    suspend fun loginUser(
        @Body body: LoginSubmitDto
    ): Response<ApiResponse<UserDetailsResponseDto>>

    @GET("api/admin/allCourses")
    suspend fun getAllCoursesDetails(
    ): Response<ApiResponse<List<CourseDetailDto>>>

    @DELETE("api/admin/courseDetails/{courseId}")
    suspend fun deleteCourseDetails(
        @Path("courseId") courseId: String,
        ): Response<ApiResponse<Unit>>

    @DELETE("api/admin/videos/{videoId}")
    suspend fun deleteVideoDelete(
        @Path("videoId") videoId: String,
    ): Response<ApiResponse<Unit>>

    @GET("api/admin/retryVideo")
    suspend fun retryVideo(
        @Query("video_id") videoId: String,
    ): Response<ApiResponse<Boolean>>

    @GET("api/admin/purchaseDetails")
    suspend fun getPurchaseDetails(
       @Query("course_id") params: String
    ): Response<ApiResponse<List<PurchaseDetailsDto>>>

    @POST("api/admin/purchaseSpec")
    suspend fun purchaseInsert(
        @Body purchaseSubmitDto: PurchaseSubmitDto
    ): Response<ApiResponse<Unit>>

    @POST("api/admin/purchaseSpec/{purchaseId}?_method=PUT")
    suspend fun purchaseDetailsUpdate(
        @Path("purchaseId") purchaseId: String,
        @Body purchaseDetailsUpdateDto: PurchaseDetailsUpdateDto
    ): Response<ApiResponse<Unit>>

    @DELETE("api/admin/purchaseDetails/{purchaseId}")
    suspend fun purchaseDetailsDelete(
        @Path("purchaseId") purchaseId: String,
    ): Response<ApiResponse<Unit>>

    @GET("api/admin/banner")
    suspend fun getBanners(
        @Query("keywords") keywords: String
    ): Response<ApiResponse<List<BannerDto>>>

    @DELETE("api/admin/banner/{banner_id}")
    suspend fun deleteBanner(
        @Path("banner_id") bannerId: String
    ): Response<ApiResponse<Unit>>

    @GET("api/admin/allNotification")
    suspend fun getNotification(
    ): Response<ApiResponse<List<NotificationDataDto>>>

//    @POST("api/splash")
//    suspend fun getUserInSplash(): Response<ApiResponse<UserDetailsResponseDto>>

    @FormUrlEncoded
    @POST("api/admin/notification")
    suspend fun notificationInsert(
        @Field("notice_text") notificationText: String
    ): Response<ApiResponse<Unit>>

    @POST("api/admin/courseIsLive/{course_id}?_method=PUT")
    suspend fun courseUpdateLive(
        @Path("course_id") courseId: String
    ): Response<ApiResponse<Unit>>

    @DELETE("api/admin/notification/{notificationId}")
    suspend fun notificationDelete(
        @Path("notificationId") notificationId: String
    ): Response<ApiResponse<Unit>>

    @GET("api/admin/allCourseVideos")
    suspend fun getCoursesVideoDetails(
        @Query("course_id") courseId: String
    ): Response<ApiResponse<List<CoursesVideoDto>>>

    @GET("api/admin/videos")
    suspend fun getVideoDetails(
        @Query("keywords") keywords: String
    ): Response<ApiResponse<List<VideoDataDto>>>

    @DELETE("api/admin/courseVideo/{video_id}")
    suspend fun courseVideoDelete(
        @Path("video_id") videoId: String
    ): Response<ApiResponse<Unit>>

    @POST("api/admin/logout")
    suspend fun getLogoutUser(
    ): Response<ApiResponse<UserDetailsResponseDto>>

    @POST("api/admin/splash")
    suspend fun getUserInSplash(): Response<ApiResponse<UserDetailsResponseDto>>


}