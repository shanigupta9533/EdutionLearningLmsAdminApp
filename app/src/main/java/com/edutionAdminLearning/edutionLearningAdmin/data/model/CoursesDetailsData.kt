package com.edutionAdminLearning.edutionLearningAdmin.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CoursesDetailsData(
    val courseDesc: String,
    val courseImage: String,
    val courseName: String,
    val coursePriceDetails: String,
    val createdAt: String,
    val isLive: Boolean,
    val id: String,
    val updatedAt: String
)
