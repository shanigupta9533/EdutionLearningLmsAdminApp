package com.edutionLearning.core.type

import com.google.gson.Gson

fun <T: Any> String.jsonToObject(clazz: Class<T>): T =
    Gson().fromJson(this, clazz)

fun <T : Any> T.toJsonString(): String =
    Gson().toJson(this)