package com.edutionLearning.core.result

import android.os.Parcelable
import com.edutionLearning.core.type.EMPTY
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Error(
    @SerializedName("status") val _status: Int?,
    @SerializedName("message") val _message: String?,
    @SerializedName("warningType") val warningType: String? = null
) : Parcelable {
    val status get() = _status ?: 0
    val message get() = _message ?: String.EMPTY
}