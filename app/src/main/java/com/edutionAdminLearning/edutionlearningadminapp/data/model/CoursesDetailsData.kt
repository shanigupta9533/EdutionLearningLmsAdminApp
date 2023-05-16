package com.edutionAdminLearning.edutionlearningadminapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoursesDetailsData(
    val courseDesc: String,
    val courseImage: String,
    val courseName: String,
    val coursePriceDetails: String,
    val createdAt: String,
    val id: String,
    val updatedAt: String
) : Parcelable
