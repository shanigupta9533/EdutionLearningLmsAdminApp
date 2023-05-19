package com.edutionAdminLearning.edutionLearningAdmin.data.mapper

import com.edutionAdminLearning.core.domain.Mapper
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.VideoDetailsDto
import com.edutionAdminLearning.edutionLearningAdmin.data.model.CoursesVideo
import com.edutionAdminLearning.edutionLearningAdmin.data.model.VideoDetails
import javax.inject.Inject

class VideoDetailsMapper @Inject constructor() : Mapper<VideoDetails?, VideoDetailsDto?, Unit>() {
    override fun dtoToDomain(dto: VideoDetailsDto?): VideoDetails? {
        return VideoDetails(
            askDoubtNumber = dto?.askDoubtNumber.value,
            courseVideo = dto?.courseVideo?.map {
                CoursesVideo(
                    id = it.id.value,
                    courseId = it.courseId.value,
                    codeLink = it.codeLink.value,
                    homeWorkLink = it.homeWorkLink.value,
                    projectLink = it.projectLink.value,
                    createdAt = it.createdAt.value,
                    updatedAt = it.updatedAt.value,
                    videoName = it.videoName.value,
                    videoLink = it.videoLink.value,
                    isVideoPlaying = it.isVideoPlaying ?: false,
                    timestamp = it.timestamp ?: "0"
                )
            } ?: emptyList()
        )
    }

    override fun domainToDto(domain: VideoDetails?): VideoDetailsDto {
        return VideoDetailsDto()
    }
}