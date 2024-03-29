package com.edutionAdminLearning.edutionLearningAdmin.data.mapper

import com.edutionAdminLearning.core.domain.Mapper
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.NotificationDataDto
import com.edutionAdminLearning.edutionLearningAdmin.data.model.NotificationData
import javax.inject.Inject

class NotificationMapper @Inject constructor() : Mapper<NotificationData, NotificationDataDto, Unit>() {
    override fun dtoToDomain(dto: NotificationDataDto): NotificationData {
        return NotificationData(
            id = dto.id.value,
            createdAt = dto.createdAt.value,
            noticeText = dto.noticeText.value,
            seen = dto.seen ?: false
        )
    }

    override fun domainToDto(domain: NotificationData): NotificationDataDto {
       return NotificationDataDto()
    }
}