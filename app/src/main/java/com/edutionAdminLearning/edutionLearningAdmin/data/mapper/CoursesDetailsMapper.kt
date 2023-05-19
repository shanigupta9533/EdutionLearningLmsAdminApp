package com.edutionAdminLearning.edutionLearningAdmin.data.mapper

import com.edutionAdminLearning.core.domain.Mapper
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.CourseDetailDto
import com.edutionAdminLearning.edutionLearningAdmin.data.model.CoursesDetailsData
import javax.inject.Inject

class CoursesDetailsMapper @Inject constructor() : Mapper<CoursesDetailsData, CourseDetailDto, Unit>() {
    override fun dtoToDomain(dto: CourseDetailDto): CoursesDetailsData {
        return CoursesDetailsData(
            courseDesc = dto.courseDesc.value,
            courseImage = dto.courseImage.value,
            courseName = dto.courseName.value,
            coursePriceDetails = dto.coursePriceDetails.value,
            createdAt = dto.createdAt.value,
            id = dto.id.value,
            updatedAt = dto.updatedAt.value,
        )

    }

    override fun domainToDto(domain: CoursesDetailsData): CourseDetailDto {
        return CourseDetailDto()
    }
}