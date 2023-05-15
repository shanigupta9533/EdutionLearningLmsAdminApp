package com.edutionAdminLearning.core.network

import android.content.Context

class PWService(
        context: Context, baseUrl: String,
        authToken: () -> String, // Barrier token is required to call authorised APIs
) : BaseService(baseUrl, context, authToken,null) {

    public override fun <T> create(javaClass: Class<T>): T {
        return super.create(javaClass)
    }

}