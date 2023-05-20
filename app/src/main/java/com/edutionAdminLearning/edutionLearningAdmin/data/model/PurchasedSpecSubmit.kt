package com.edutionAdminLearning.edutionLearningAdmin.data.model

import com.google.gson.annotations.SerializedName

data class PurchasedSpecSubmitDto(
    @SerializedName("spec_name")
    var specName: String,
    @SerializedName("available")
    var available: Boolean
)
