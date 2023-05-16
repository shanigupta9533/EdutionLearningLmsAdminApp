package com.edutionAdminLearning.edutionlearningadminapp.data.dto

import com.google.gson.annotations.SerializedName

data class CourseDetailDto(
    @SerializedName("course_desc")
    val courseDesc: String?=null,
    @SerializedName("picture_data")
    val courseImage: String?=null,
    @SerializedName("course_name")
    val courseName: String?=null,
    @SerializedName("course_price_details")
    val coursePriceDetails: String?=null,
    @SerializedName("created_at")
    val createdAt: String?=null,
    @SerializedName("id")
    val id: String?=null,
    @SerializedName("updated_at")
    val updatedAt: String?=null,
)