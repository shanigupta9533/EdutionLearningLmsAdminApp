package com.edutionAdminLearning.edutionLearningAdmin.data.dto

import com.google.gson.annotations.SerializedName

data class PurchaseSubmitDto(
    @SerializedName("course_id")
    val courseId: String? = null,
    @SerializedName("course_type")
    val courseType: String? = null,
    @SerializedName("price")
    val price: String? = null,
    @SerializedName("purchaseSpec")
    val purchaseSpec: List<PurchaseSubmitSpecDto?>? = null
)

data class PurchaseSubmitSpecDto(
    @SerializedName("available")
    val available: Boolean? = null,
    @SerializedName("spec_name")
    val specName: String? = null
)