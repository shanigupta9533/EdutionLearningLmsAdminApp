package com.edutionAdminLearning.edutionLearningAdmin.data.mapper

import com.edutionAdminLearning.core.domain.Mapper
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.VideoDataDto
import com.edutionAdminLearning.edutionLearningAdmin.data.model.VideoData
import javax.inject.Inject

class VideoDetailsMapper @Inject constructor(
) : Mapper<VideoData?, VideoDataDto?, Unit>() {

    override fun dtoToDomain(dto: VideoDataDto?): VideoData? {
        return VideoData(
            id = dto?.id.value,
            videoName = dto?.videoName.value,
            videoUniqueName = dto?.videoUniqueName.value,
            status = dto?.status ?: false,
            videoLocate = dto?.videoLocate.value,
            createdAt = dto?.createdAt.value,
            updatedAt = dto?.updatedAt.value,
            videoData = dto?.videoData.value,
            failed = dto?.failed ?: true
        )
    }

    override fun domainToDto(domain: VideoData?): VideoDataDto? {
        return VideoDataDto()
    }
}