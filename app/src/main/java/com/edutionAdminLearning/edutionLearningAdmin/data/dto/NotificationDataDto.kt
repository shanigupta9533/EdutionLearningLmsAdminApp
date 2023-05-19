package com.edutionAdminLearning.edutionLearningAdmin.data.dto

import com.google.gson.annotations.SerializedName

data class NotificationDataDto(

    @SerializedName("id")
    var id: String?=null,

    @SerializedName("notice_text")
    var noticeText: String?=null,

    @SerializedName("created_at")
    var createdAt: String?=null,

    @SerializedName("seen")
    var seen: Boolean?=null

)

