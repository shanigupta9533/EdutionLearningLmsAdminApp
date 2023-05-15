package com.edutionLearning.core.result

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: T,
    @SerializedName("message")
    val message: String,
    @SerializedName("error")
    val error: Error? = null,
    @SerializedName("paginate")
    var paginate: PagingResponse? = null
)

data class PagingResponse(
    @SerializedName("totalCount") var totalCount: Int? = null
)