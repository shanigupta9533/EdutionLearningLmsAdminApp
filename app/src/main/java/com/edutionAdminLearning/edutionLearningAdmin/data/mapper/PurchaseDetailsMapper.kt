package com.edutionAdminLearning.edutionLearningAdmin.data.mapper

import com.edutionAdminLearning.core.domain.Mapper
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseDetailsDto
import com.edutionAdminLearning.edutionLearningAdmin.data.model.PurchaseDetails
import com.edutionAdminLearning.edutionLearningAdmin.data.model.PurchaseSpec
import javax.inject.Inject

class PurchaseDetailsMapper @Inject constructor() : Mapper<PurchaseDetails, PurchaseDetailsDto, Unit>() {
    override fun dtoToDomain(dto: PurchaseDetailsDto): PurchaseDetails {
        return PurchaseDetails(
            courseId = dto.courseId.value,
            courseType = dto.courseType.value,
            id = dto.id ?: 0,
            price = dto.price.value,
            purchaseSpec = dto.purchaseSpec?.map {
                PurchaseSpec(
                    available = it?.available ?: false,
                    courseTypeId = it?.courseTypeId.value,
                    id = it?.id ?: 0,
                    specName = it?.specName.value
                )
            } ?: emptyList()
        )
    }

    override fun domainToDto(domain: PurchaseDetails): PurchaseDetailsDto {
        return PurchaseDetailsDto()
    }

}