package com.edutionAdminLearning.edutionLearningAdmin.data.mapper

import com.edutionAdminLearning.core.domain.Mapper
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.CoursesVideoDto
import com.edutionAdminLearning.edutionLearningAdmin.data.model.CoursesVideo
import javax.inject.Inject

class CoursesVideoDetailsMapper @Inject constructor() : Mapper<CoursesVideo, CoursesVideoDto, Unit>() {
    override fun dtoToDomain(dto: CoursesVideoDto): CoursesVideo {
        return dto.let {
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
                timestamp = it.timestamp ?: "0",
                displayOrder = it.displayOrder ?: "0",
                videoId = it.videoId.value,
                lectureName = it.lectureName.value
            )
        }
    }

    override fun domainToDto(domain: CoursesVideo): CoursesVideoDto {
        return CoursesVideoDto()
    }
}