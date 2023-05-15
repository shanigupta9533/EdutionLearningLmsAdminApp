package com.edutionLearning.network.managers

import android.content.Context
import com.edutionLearning.network.utils.LoginManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkManager @Inject constructor(
    @ApplicationContext context: Context
) {

   val loginManager = LoginManager.getInstance(context)

}