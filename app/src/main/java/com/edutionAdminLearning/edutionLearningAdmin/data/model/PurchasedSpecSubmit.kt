package com.edutionAdminLearning.edutionLearningAdmin.data.model

import com.edutionAdminLearning.core.type.EMPTY

data class PurchasedSpecSubmit(
    var specName: String = String.EMPTY,
    var available: Boolean = false,
    val purchaseSpecId: String? = null,
)
