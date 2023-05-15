package com.edutionLearning.core.network

import android.content.Context
import com.edutionLearning.core.network.HttpCacheBuilder.HEADER_CACHE_ENABLED_KEY
import com.edutionLearning.core.network.HttpCacheBuilder.HEADER_CACHE_ENABLED_VALUE
import com.edutionLearning.core.network.HttpCacheBuilder.HEADER_CACHE_ENABLED_VALUE_DISABLED
import com.edutionLearning.core.network.HttpCacheBuilder.HEADER_CACHE_MAX_AGE_KEY
import com.edutionLearning.core.network.HttpCacheBuilder.HEADER_CACHE_MAX_STALE_KEY
import com.edutionLearning.core.network.HttpCacheBuilder.firebaseControlMaxAge
import com.edutionLearning.core.utils.CoroutineHelper
import okhttp3.*
import java.util.concurrent.TimeUnit

object HttpCacheBuilder {

    const val DEFAULT_MAX_AGE = 3600

    @JvmStatic
    var appLevelCacheEnable = true

    @JvmStatic
    var firebaseControlEnabled = true

    @JvmStatic
    var firebaseControlMaxAge: Long = 60

    internal val cachedAPI = mutableMapOf<String, Boolean>()
    internal var cache : Cache ?= null

    private val cacheSize by lazy { (10 * 1024 * 1024).toLong() } // 10 MB
    const val HEADER_CACHE_ENABLED_KEY = "WP-Check-Cache-Enabled"
    const val HEADER_CACHE_ENABLED_VALUE = "Enabled"
    const val HEADER_CACHE_ENABLED_VALUE_DISABLED = "Disabled"
    const val HEADER_CACHE_ENABLED = "$HEADER_CACHE_ENABLED_KEY: $HEADER_CACHE_ENABLED_VALUE"
    const val HEADER_CACHE_DISABLED = "$HEADER_CACHE_ENABLED_KEY: $HEADER_CACHE_ENABLED_VALUE_DISABLED"

    // Using HEADER_CACHE_MAX_AGE_KEY in header will ignore the firebase maxAge handle
    const val HEADER_CACHE_MAX_AGE_KEY =
        "WP-Check-maxAge" // Value should be in seconds, ex - 60, 30, 3600
    const val HEADER_CACHE_MAX_STALE_KEY =
        "WP-Check-maxStale" // Value should be in days, ex - 1, 2, 3, 4

    @JvmStatic
    fun attach(context: Context, builder: OkHttpClient.Builder) {
            cache = Cache(context.cacheDir, cacheSize)
            builder.addInterceptor(officeNetworkCacheInterceptor(hasNetwork = { hasNetwork(context) }))
            builder.addNetworkInterceptor(onlineNetworkCacheInterceptor())
            builder.cache(cache)
    }

    private fun hasNetwork(context: Context): Boolean = NetworkUtils.isConnectedToInternet(context)

    fun clearHTTPCache() {
        CoroutineHelper.create().launchOnIO {
            cachedAPI.clear()
            cache?.evictAll()
        }
    }

}

private const val HEADER_CACHE_CONTROL = "Cache-Control"
private const val HEADER_PRAGMA = "Pragma"
private const val REQUEST_METHOD_GET = "GET"

fun onlineNetworkCacheInterceptor() = Interceptor { chain ->
    val request = chain.request()
    if (HttpCacheBuilder.firebaseControlEnabled && request.method == REQUEST_METHOD_GET && request.isCacheEnabled()) {
        val cacheControl: CacheControl = CacheControl.Builder()
            .maxAge(
                request.getCacheMaxAge()?.toInt() ?: firebaseControlMaxAge.toInt(),
                TimeUnit.SECONDS
            )
            .build()

        // If cache is enabled
        chain.proceed(request).newBuilder().apply {
            removeHeader(HEADER_PRAGMA)
            removeHeader(HEADER_CACHE_CONTROL)
            header(
                HEADER_CACHE_CONTROL,
                cacheControl.toString()
            )
        }.build()
    } else chain.proceed(request)
}

fun officeNetworkCacheInterceptor(
    hasNetwork: () -> Boolean,
    maxStale: Int = 7
) = Interceptor { chain ->
    val request = chain.request()
    // If cache is enabled
    chain.proceed(
        if (HttpCacheBuilder.firebaseControlEnabled && request.method == REQUEST_METHOD_GET && request.isCacheEnabled()) {
            if (!hasNetwork()) request.newBuilder()
                .apply {
                    removeHeader(HEADER_PRAGMA)
                    removeHeader(HEADER_CACHE_CONTROL)
                    val cacheControl = CacheControl.Builder()
                        .maxAge(
                            (request.getCacheMaxStale()?.toInt() ?: maxStale),
                            TimeUnit.DAYS
                        )
                        .build()
                    cacheControl(cacheControl)
                }.build()
            // Check if API is cached at app leve
            else request.newBuilder().apply {
                val shouldCallAPI =
                    HttpCacheBuilder.appLevelCacheEnable
                            && !(HttpCacheBuilder.cachedAPI[request.url.toString()] ?: false)
                if (shouldCallAPI) {
                    HttpCacheBuilder.cachedAPI[request.url.toString()] = true
                    removeHeader(HEADER_PRAGMA)
                    removeHeader(HEADER_CACHE_CONTROL)
                    cacheControl(CacheControl.FORCE_NETWORK)
                }
            }.build()
        } else if (HttpCacheBuilder.firebaseControlEnabled && request.method == REQUEST_METHOD_GET && request.isCacheDisabled())
            request.newBuilder().apply {
                removeHeader(HEADER_PRAGMA)
                removeHeader(HEADER_CACHE_CONTROL)
                cacheControl(CacheControl.FORCE_NETWORK)
            }.build()
        else request
    )
}


fun Request.isCacheEnabled() =
    header(HEADER_CACHE_ENABLED_KEY)?.let {
        it == HEADER_CACHE_ENABLED_VALUE
    } ?: false

fun Request.isCacheDisabled() =
    header(HEADER_CACHE_ENABLED_KEY)?.let {
        it == HEADER_CACHE_ENABLED_VALUE_DISABLED
    } ?: false

fun Request.getCacheMaxAge() =
    header(HEADER_CACHE_MAX_AGE_KEY)

fun Request.getCacheMaxStale() =
    header(HEADER_CACHE_MAX_STALE_KEY)