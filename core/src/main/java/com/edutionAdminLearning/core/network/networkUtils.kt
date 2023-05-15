package com.edutionAdminLearning.core.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.webkit.WebSettings
import com.edutionAdminLearning.core.utils.CoreConstant
import com.google.gson.JsonObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.Request

const val TIME_DURATION = 4000L
const val NOTIFICATION_DURATION = 50000L

class NetworkUtils(private val context: Context?) {

    companion object {
        private var currentVersion = ""

        @JvmStatic
        fun isConnectedToInternet(context: Context): Boolean {
            val connectivity =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                try {
                    val info = connectivity.allNetworkInfo
                    for (networkInfo in info) if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                } catch (e: Exception) {
                    return false
                }
            }
            return false
        }

        @JvmStatic
        fun checkCallbackHandler() = callbackFlow {
            val handler = Handler(Looper.myLooper()!!)
            val callback = object : Runnable {
                override fun run() {
                    handler.postDelayed(this, TIME_DURATION)
                    trySend(true)
                }
            }

            handler.postDelayed(callback, TIME_DURATION)

            awaitClose {
                handler.removeCallbacks(callback)
            }

        }

        @JvmStatic
        fun notificationTimer() = callbackFlow {
            val handler = Handler(Looper.myLooper()!!)
            val callback = object : Runnable {
                override fun run() {
                    handler.postDelayed(this, NOTIFICATION_DURATION)
                    trySend(true)
                }
            }

            handler.postDelayed(callback, NOTIFICATION_DURATION)
            trySend(true)

            awaitClose {
                handler.removeCallbacks(callback)
            }

        }
    }

    fun getUserAgent(): String {
        if (com.edutionAdminLearning.core.BuildConfig.DEBUG) {
            return "Android"
        }

        return try {
            WebSettings.getDefaultUserAgent(context)
        } catch (ex: Exception) {
            "Android";
        }
    }

    fun getRequest(original: Request, authToken: () -> String): Request {
        val token = authToken()
        return original.newBuilder()
            .removeHeader(NetworkHeaders.CONTENT_TYPE)
            .header(NetworkHeaders.CONTENT_TYPE, "application/json")
            .header(
                "Authorization",
                "Bearer $token"
            )
            .header(
                "client-version",
                getAppVersionCode()
            ).header("user-agent", getUserAgent())
            .header("randomId", randomId!!)
            .header("Client-Type", NetworkHeaders.DEVICE_TYPE_VALUE)
            .header("device-meta", getMiscData())
            .header(CoreConstant.REFERER, CoreConstant.ANDROID_LIVE)
            .method(original.method, original.body)
            .build()
    }


    fun getDeviceModel(): String {
        val model = Build.MODEL
        return capitalize(model)
    }

    fun getDeviceMake(): String {
        val manufacturer = Build.MANUFACTURER
        return capitalize(manufacturer)
    }

    fun getDeviceOs(): String {
        return Build.VERSION.RELEASE
    }

    private fun getPackageName(): String {
        return context!!.applicationContext.packageName
    }

    fun capitalize(s: String?): String {
        if (s == null || s.isEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }

    fun getMiscData(): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty(NetworkHeaders.APP_VERSION, getAppVersionCode())
        jsonObject.addProperty(NetworkHeaders.APP_VERSION_NAME, getAppVersion())
        jsonObject.addProperty(NetworkHeaders.DEVICE_MAKE, getDeviceMake())
        jsonObject.addProperty(NetworkHeaders.DEVICE_MODEL, getDeviceModel())
        jsonObject.addProperty(NetworkHeaders.OS_VERSION, getDeviceOs()) //getDeviceOs());
        jsonObject.addProperty(NetworkHeaders.PACKAGE_NAME, getPackageName())
        return jsonObject.toString()
    }

    fun getAppVersion(): String {
        if (!TextUtils.isEmpty(currentVersion)) {
            return currentVersion
        }
        try {
            val pInfo = context!!.packageManager.getPackageInfo(
                context.packageName, 0
            )
            currentVersion = pInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return currentVersion
    }

    @get:SuppressLint("HardwareIds")
    val randomId: String?
        get() = if (context == null) null else Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

    fun getAppVersionCode(): String {
        var getVersionCode = ""
        try {
            val pInfo = context!!.packageManager.getPackageInfo(
                context.packageName, 0
            )
            getVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pInfo.longVersionCode.toString()
            } else pInfo.versionCode.toString()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return getVersionCode
    }

}