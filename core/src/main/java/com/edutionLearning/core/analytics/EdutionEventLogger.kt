package com.edutionLearning.core.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.edutionLearning.core.BuildConfig
import com.edutionLearning.core.utils.ConstantKeys
import com.google.firebase.analytics.FirebaseAnalytics

open class EdutionEventLogger(
    private val context: Context,
    private val userId: () -> String,
    private val mobileNumber: () -> String
) {

    private fun getFirebase(): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Synchronized
    open fun MutableMap<String, Any>.logFirebase(eventType: String) {
        //todo
        if (!com.edutionLearning.core.BuildConfig.DEBUG) {
            getFirebase().logEvent(eventType, intoBundle())
        } else {
            Log.d("Analytics", "Analytics disabled for debug")
        }
    }

    private fun MutableMap<String, Any>.intoBundle(): Bundle {
        val it: Iterator<Map.Entry<String, Any>> = entries.iterator()
        val bundle = Bundle()
        while (it.hasNext()) {
            val (key, value) = it.next()
            bundle.putString(key, value.toString())
        }
        addBaseParams(bundle)
        return bundle
    }

    private fun addBaseParams(bundle: Bundle) {
        bundle.putString(ConstantKeys.USER_ID, userId())
        // we'll not send mobile number in future.
        // bundle.putString(ConstantKeys.USER_PHONE, mobileNumber())
    }

}