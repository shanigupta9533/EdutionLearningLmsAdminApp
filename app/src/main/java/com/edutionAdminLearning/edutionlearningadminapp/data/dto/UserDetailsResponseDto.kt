package com.edutionAdminLearning.edutionlearningadminapp.data.dto

import com.google.gson.annotations.SerializedName

data class UserDetailsResponseDto(

    @SerializedName("name")
    val name: String?=null,

    @SerializedName("phone")
    val phone:String?=null,

    @SerializedName("timestamp")
    val timestamp:String?=null,

    @SerializedName("id")
    val userId:String?=null,

    @SerializedName("token")
    val token:String?=null,

    @SerializedName("admin_verified")
    val adminVerified: Boolean?=null

)
