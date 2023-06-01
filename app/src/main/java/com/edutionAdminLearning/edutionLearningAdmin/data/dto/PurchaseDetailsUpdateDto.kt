package com.edutionAdminLearning.edutionLearningAdmin.data.dto

import com.google.gson.annotations.SerializedName

data class PurchaseDetailsUpdateDto(
    @SerializedName("course_id")
    val courseId: String? = null,
    @SerializedName("course_type")
    val courseType: String? = null,
    @SerializedName("course_type_id")
    val courseTypeId: String? = null,
    @SerializedName("price")
    val price: String? = null,
    @SerializedName("purchaseSpecList")
    val purchaseSpecList: List<PurchaseSpecUpdateDto?>? = null
)

data class PurchaseSpecUpdateDto(
    @SerializedName("available")
    val available: Boolean? = null,
    @SerializedName("purchase_spec_id")
    val purchaseSpecId: String? = null,
    @SerializedName("spec_name")
    val specName: String? = null
)