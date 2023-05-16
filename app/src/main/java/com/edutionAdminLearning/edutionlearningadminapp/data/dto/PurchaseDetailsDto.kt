package com.edutionAdminLearning.edutionlearningadminapp.data.dto


import com.google.gson.annotations.SerializedName

data class PurchaseDetailsDto(
    @SerializedName("course_id")
    val courseId: String? = null,
    @SerializedName("course_type")
    val courseType: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("price")
    val price: String? = null,
    @SerializedName("purchase_spec")
    val purchaseSpec: List<PurchaseSpecDto?>? = null
)

data class PurchaseSpecDto(
    @SerializedName("available")
    val available: Boolean? = null,
    @SerializedName("course_type_id")
    val courseTypeId: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("spec_name")
    val specName: String? = null
)