package com.edutionAdminLearning.edutionLearningAdmin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PurchaseDetails(
    val courseId: String,
    val courseType: String,
    val id: String,
    val price: String,
    val purchaseSpec: List<PurchaseSpec>
) : Parcelable

@Parcelize
data class PurchaseSpec(
    val available: Boolean,
    val courseTypeId: String,
    val id: String,
    val specName: String
) : Parcelable