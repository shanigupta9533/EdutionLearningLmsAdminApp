package com.edutionAdminLearning.edutionlearningadminapp.data.mapper

import com.edutionAdminLearning.core.domain.Mapper
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.UserDetailsResponseDto
import com.edutionAdminLearning.edutionlearningadminapp.data.model.UserDetailsData
import javax.inject.Inject

class UserDetailsMapper  @Inject constructor() : Mapper<UserDetailsData?, UserDetailsResponseDto?, Unit>() {
    override fun dtoToDomain(dto: UserDetailsResponseDto?): UserDetailsData? {
        return UserDetailsData(
            name = dto?.name.value,
            phone = dto?.phone.value,
            timestamp = dto?.timestamp.value,
            token = dto?.token.value,
            userId = dto?.userId.value,
            adminVerified = dto?.adminVerified ?: false
        )
    }

    override fun domainToDto(domain: UserDetailsData?): UserDetailsResponseDto {
        return UserDetailsResponseDto()
    }

}