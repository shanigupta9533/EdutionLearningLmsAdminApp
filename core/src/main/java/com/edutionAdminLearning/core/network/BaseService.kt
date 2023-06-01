package com.edutionAdminLearning.core.network

import android.content.Context
import com.edutionAdminLearning.core.result.NoConnectivityException
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.mocklets.pluto.PlutoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


/**
 * Base service class is used to create retrofit network
 */
open class BaseService protected constructor(
    baseUrl: String,
    private val context: Context,
    private val authToken: () -> String, // Barrier token is required to call authorised APIs
    callAdapterFactory: CallAdapter.Factory?

) {
    private val mRetrofit: Retrofit
    private val networkUtils: NetworkUtils = NetworkUtils(context)


    private fun getAuthHttpClient(): OkHttpClient.Builder {
        val httpClient = OkHttpClient.Builder()
        HttpCacheBuilder.attach(context, httpClient)
        httpClient.readTimeout(120, TimeUnit.SECONDS)
        httpClient.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val request = networkUtils.getRequest(chain.request(), authToken)
            NetworkCallStatus.postServerDown(false)
            chain.proceed(request = request)
        })
        if (com.edutionAdminLearning.core.BuildConfig.DEBUG) {
            val mLogging = HttpLoggingInterceptor()
            mLogging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(mLogging)
        }
        httpClient.addInterceptor(PlutoInterceptor())
        httpClient.addNetworkInterceptor(StethoInterceptor())
        // Throw for network call
        httpClient.addInterceptor {
            val request = it.request()
            val isConnected = NetworkUtils.isConnectedToInternet(context)
            if (!isConnected && !request.isCacheEnabled())
                throw NoConnectivityException()
            else {
                // TODO Check if data is available not in cache, then show network error
            }
            it.proceed(request)
        }
        httpClient.addInterceptor(object : NetworkCallStatusCodeInterceptor() {
            override fun onServerCrashed() {
                NetworkCallStatus.postServerDown(true)
            }

            override fun onServerWorking() {
                NetworkCallStatus.postServerDown(false)
            }

            override fun onStatus(code: Pair<Int, String>) {
                NetworkCallStatus.postCallStatusCode(code)
            }
        })
        return httpClient
    }

    protected open fun <T> create(javaClass: Class<T>): T {
        return mRetrofit.create(javaClass)
    }

    init {
        val gson = GsonBuilder().setLenient().create()
        val client: OkHttpClient = getAuthHttpClient().build()
        val builder = Retrofit.Builder()
        builder.baseUrl(baseUrl)
        if (callAdapterFactory != null) {
            builder.addCallAdapterFactory(callAdapterFactory)
        }
        builder.addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
        mRetrofit = builder.build()
    }
}