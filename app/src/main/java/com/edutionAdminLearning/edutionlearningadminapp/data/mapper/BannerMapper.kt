package com.edutionAdminLearning.edutionlearningadminapp.data.mapper

import com.edutionAdminLearning.core.domain.Mapper
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.BannerDto
import com.edutionAdminLearning.edutionlearningadminapp.data.model.BannerData
import javax.inject.Inject

class BannerMapper @Inject constructor() : Mapper<BannerData, BannerDto, Unit>() {
    override fun dtoToDomain(dto: BannerDto): BannerData {
        return BannerData(
            createdAt = dto.createdAt.value,
            id = dto.id.value,
            image = dto.image.value,
            keywords = dto.keywords.value,
            messageText = dto.messageText.value,
            updatedAt = dto.updatedAt.value
        )
    }

    override fun domainToDto(domain: BannerData): BannerDto {
        return BannerDto()
    }
}