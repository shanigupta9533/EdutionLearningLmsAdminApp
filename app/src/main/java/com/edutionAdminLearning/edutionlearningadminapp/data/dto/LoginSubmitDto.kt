package com.edutionAdminLearning.edutionlearningadminapp.data.dto

import com.google.gson.annotations.SerializedName

data class LoginSubmitDto(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("password")
    val password: String
)
