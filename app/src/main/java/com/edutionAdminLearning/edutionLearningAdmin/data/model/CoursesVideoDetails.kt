package com.edutionAdminLearning.edutionLearningAdmin.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoursesVideo(
    val id: String,
    val videoName:String,
    val lectureName:String,
    val videoId:String,
    val videoLink:String,
    val homeWorkLink: String,
    val displayOrder: String,
    val projectLink:String,
    val codeLink:String,
    val courseId:String,
    val isVideoPlaying: Boolean,
    val timestamp: String,
    val createdAt:String,
    val updatedAt:String
) : Parcelable