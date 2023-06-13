package com.edutionAdminLearning.edutionLearningAdmin.di

import com.edutionAdminLearning.edutionLearningAdmin.data.model.VideoData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

object ChatEvents {

    private val mVideoEvents = MutableStateFlow<VideoData?>(null)
    val onVideoEvents = mVideoEvents.asSharedFlow()

    fun onVideoSelected(videoDetails: VideoData?) {
        mVideoEvents.value = videoDetails
    }

}