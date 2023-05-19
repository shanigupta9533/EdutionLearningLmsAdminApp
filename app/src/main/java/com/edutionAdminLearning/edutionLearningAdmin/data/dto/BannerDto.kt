package com.edutionAdminLearning.edutionLearningAdmin.data.dto

import com.google.gson.annotations.SerializedName

data class BannerDto(
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("picture_data")
    val image: String? = null,
    @SerializedName("keywords")
    val keywords: String? = null,
    @SerializedName("messageText")
    val messageText: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
)