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
            videoEmbedUrl = dto?.videoEmbedUrl.value,
            status = dto?.status ?: false,
            videoUrl = dto?.videoUrl.value,
            createdAt = dto?.createdAt.value,
            updatedAt = dto?.updatedAt.value,
        )
    }

    override fun domainToDto(domain: VideoData?): VideoDataDto? {
        return VideoDataDto()
    }
}