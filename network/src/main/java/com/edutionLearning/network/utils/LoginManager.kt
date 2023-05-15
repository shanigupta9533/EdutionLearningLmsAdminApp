package com.edutionLearning.network.utils

import android.content.Context
import com.edutionLearning.core.network.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val sharedPreferences = com.edutionLearning.network.utils.SharedPrefsManagerInternal(context)

    companion object {
        var INSTANCE: LoginManager? = null

        @JvmStatic
        fun getInstance(context: Context): LoginManager? {
            if (INSTANCE == null) {
                INSTANCE = LoginManager(context)
            }
            return INSTANCE
        }
    }

    fun setToken(token: String?) {
        sharedPreferences.put(com.edutionLearning.network.utils.SharedPrefsManagerInternal.Key.AUTH_TOKEN, token)
        sharedPreferences.putString(NetworkUtils(context).randomId, token)
    }

    fun setName(name: String) {
        sharedPreferences.put(com.edutionLearning.network.utils.SharedPrefsManagerInternal.Key.USER_NAME, name)
    }

    fun setPhone(phone: String) {
        sharedPreferences.put(com.edutionLearning.network.utils.SharedPrefsManagerInternal.Key.USER_MOBILE, phone)
    }

    fun setUserId(userId: String) {
        sharedPreferences.put(com.edutionLearning.network.utils.SharedPrefsManagerInternal.Key.USER_ID, userId)
    }

    fun logout() {
        sharedPreferences.clear()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(com.edutionLearning.network.utils.SharedPrefsManagerInternal.Key.USER_NAME)
    }

    fun getPhone(): String? {
        return sharedPreferences.getString(com.edutionLearning.network.utils.SharedPrefsManagerInternal.Key.USER_MOBILE)
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(com.edutionLearning.network.utils.SharedPrefsManagerInternal.Key.USER_ID)
    }

    fun getToken(): String? {
        return sharedPreferences.getString(com.edutionLearning.network.utils.SharedPrefsManagerInternal.Key.AUTH_TOKEN)
    }


}