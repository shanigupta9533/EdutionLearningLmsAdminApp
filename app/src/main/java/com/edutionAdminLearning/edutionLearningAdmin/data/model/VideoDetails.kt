package com.edutionAdminLearning.edutionLearningAdmin.data.model

data class VideoDetails(
    val askDoubtNumber: String,
    val courseVideo: List<CoursesVideo>
)

data class CoursesVideo(
    val id: String,
    val videoName:String,
    val videoLink:String,
    val homeWorkLink: String,
    val projectLink:String,
    val codeLink:String,
    val courseId:String,
    val isVideoPlaying: Boolean,
    val timestamp: String,
    val createdAt:String,
    val updatedAt:String
)