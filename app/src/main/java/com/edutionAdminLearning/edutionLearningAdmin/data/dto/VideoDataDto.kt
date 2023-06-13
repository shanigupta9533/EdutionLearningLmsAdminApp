package com.edutionAdminLearning.edutionLearningAdmin.data.dto

import com.google.gson.annotations.SerializedName

data class VideoDataDto(

    @SerializedName("id")
    val id: String?=null,

    @SerializedName("video_name")
    val videoName:String?=null,

    @SerializedName("video_unique_name")
    val videoUniqueName: String?=null,

    @SerializedName("status")
    val status: Boolean?=null,

    @SerializedName("failed")
    val failed: Boolean?=null,

    @SerializedName("video_locate")
    val videoLocate:String?=null,

    @SerializedName("created_at")
    val createdAt: String?=null,

    @SerializedName("updated_at")
    val updatedAt: String?=null,

    @SerializedName("video_data")
    val videoData: String?=null

)
