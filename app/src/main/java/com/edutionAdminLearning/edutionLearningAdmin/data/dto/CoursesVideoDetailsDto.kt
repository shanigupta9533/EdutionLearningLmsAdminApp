package com.edutionAdminLearning.edutionLearningAdmin.data.dto

import com.google.gson.annotations.SerializedName

data class CoursesVideoDetailsDto(

    @SerializedName("askDoubtNumber")
    val askDoubtNumber: String?=null,

    @SerializedName("coursesVideo")
    val courseVideo: List<CoursesVideoDto>?=null
)

data class CoursesVideoDto(

    @SerializedName("id")
    val id: String?=null,

    @SerializedName("video_name")
    val videoName:String?=null,

    @SerializedName("lecture_name")
    val lectureName: String?=null,

    @SerializedName("video_id")
    val videoId:String?=null,

    @SerializedName("video_data")
    val videoLink:String?=null,

    @SerializedName("home_work_link")
    val homeWorkLink: String?=null,

    @SerializedName("project_link")
    val projectLink:String?=null,

    @SerializedName("code_link")
    val codeLink:String?=null,

    @SerializedName("course_id")
    val courseId:String?=null,

    @SerializedName("isVideoPlaying")
    val isVideoPlaying:Boolean?=null,

    @SerializedName("timestamp")
    val timestamp:String?=null,

    @SerializedName("display_order")
    val displayOrder:String?=null,

    @SerializedName("created_at")
    val createdAt:String?=null,

    @SerializedName("updated_at")
    val updatedAt:String?=null
)