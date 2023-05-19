package com.edutionAdminLearning.edutionLearningAdmin.data.model

data class PurchaseDetails(
    val courseId: String,
    val courseType: String,
    val id: Int,
    val price: String,
    val purchaseSpec: List<PurchaseSpec>
)

data class PurchaseSpec(
    val available: Boolean,
    val courseTypeId: String,
    val id: Int,
    val specName: String
)