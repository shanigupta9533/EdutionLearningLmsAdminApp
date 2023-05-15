package com.edutionAdminLearning.core.network

import com.edutionAdminLearning.core.network.NetworkHeaders.DATE
import com.edutionAdminLearning.core.network.NetworkHeaders.SERVER_TIME
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

internal abstract class NetworkCallStatusCodeInterceptor : Interceptor {

    abstract fun onServerCrashed()
    abstract fun onServerWorking()
    abstract fun onStatus(code: Pair<Int, String>)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)

        // Set network date
        NetworkDate.setDate(response.header(DATE))
        NetworkDate.setServerTime(response.header(SERVER_TIME))

        onStatus(response.code to request.url.toString())
        if (response.code == 503) {
            onServerCrashed()
        } else {
            onServerWorking()
        }
        return response
    }
}