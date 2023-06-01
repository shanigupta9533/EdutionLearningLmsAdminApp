package com.edutionAdminLearning.edutionLearningAdmin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BannerData(
    val createdAt: String,
    val id: String,
    val image: String,
    val keywords: String,
    val messageText: String,
    val updatedAt: String
) : Parcelable
