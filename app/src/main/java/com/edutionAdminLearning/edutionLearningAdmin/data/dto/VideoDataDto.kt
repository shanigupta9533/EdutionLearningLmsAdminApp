package com.edutionAdminLearning.edutionLearningAdmin.data.dto

import com.google.gson.annotations.SerializedName

data class VideoDataDto(

    @SerializedName("id")
    val id: String?=null,

    @SerializedName("video_name")
    val videoName:String?=null,

    @SerializedName("video_url")
    val videoUrl: String?=null,

    @SerializedName("status")
    val status: Boolean?=null,

    @SerializedName("video_embed_url")
    val videoEmbedUrl:String?=null,

    @SerializedName("created_at")
    val createdAt: String?=null,

    @SerializedName("updated_at")
    val updatedAt: String?=null,

)
