package com.edutionAdminLearning.edutionLearningAdmin.data.model

data class VideoData(

    val id: String,
    val videoName:String,
    val videoUniqueName: String,
    val status: Boolean,
    val failed: Boolean,
    val videoLocate:String,
    val createdAt: String,
    val updatedAt: String,
    val videoData: String

)
