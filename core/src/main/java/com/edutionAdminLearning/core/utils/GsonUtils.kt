package com.edutionAdminLearning.core.utils

import com.edutionAdminLearning.core.type.EMPTY
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

var gson = GsonBuilder().setLenient().create() ?: Gson()

/*
  For Kotlin class use (list object to string)
*/
inline fun <reified T : Any> listToJson(listObj: List<T>?): String? {
    return try {
        gson.toJson(listObj ?: listOf<T>(), getTypeList(T::class.java))
    } catch (e: JsonParseException) {
        String.EMPTY
    }
}

/*
  For Kotlin/Java class use (list object to string)
*/
fun <T : Any> listToJson(listObj: List<T>?, type: Class<T>): String? {
    return try {
        gson.toJson(listObj ?: listOf<T>(), getTypeList(type))
    } catch (e: JsonParseException) {
        String.EMPTY
    }
}

/*
  For Kotlin class use (string to list object)
*/
inline fun <reified T : Any> fromJsonToList(json: String?): List<T>? {
    return try {
        gson.fromJson<List<T>>(json ?: String.EMPTY, getTypeList(T::class.java))
    } catch (e: JsonParseException) {
        listOf()
    }
}

/*
  For Kotlin/Java class use (string to list object)
*/
fun <T : Any> fromJsonToList(json: String?, type: Class<T>): List<T>? {
    return try {
        // added a null check at the end as fromJson can return null in some conditions
        gson.fromJson<List<T>>(json ?: String.EMPTY, getTypeList(type)) ?: listOf()
    } catch (e: JsonParseException) {
        listOf()
    }
}

/*
  For Kotlin class use (object to string)
*/
inline fun <reified T : Any> objectToJson(clsObj: T?): String? {
    return try {
        gson.toJson(clsObj, getTypeClass(T::class.java))
    } catch (e: JsonParseException) {
        String.EMPTY
    }
}

/*
  For Kotlin/Java class use (object to string)
*/
fun <T : Any> objectToJson(clsObj: T?, type: Class<T>): String? {
    return try {
        gson.toJson(clsObj, getTypeClass(type))
    } catch (e: JsonParseException) {
        String.EMPTY
    }
}

/*
  For Kotlin class use (string to object)
*/
inline fun <reified T : Any> fromJsonToObject(json: String?): T? {
    return try {
        gson.fromJson(json ?: String.EMPTY, getTypeClass(T::class.java))
    } catch (e: JsonParseException) {
        null
    }
}

/*
  For Kotlin/Java class use (string to object)
*/
fun <T : Any> fromJsonToObject(json: String?, type: Class<T>): T? {
    return try {
        gson.fromJson(json ?: String.EMPTY, getTypeClass(type))
    } catch (e: JsonParseException) {
        null
    }
}

/*
  For Kotlin/Java class use (map object to string)
*/
fun <T : Any> mapObjectToJson(data: Map<String, T>, type: Class<T>): String? {
    return try {
        GsonBuilder().setPrettyPrinting().create().toJson(data, getTypeClass(type))
    } catch (e: JsonParseException) {
        String.EMPTY
    }
}

/*
  For Kotlin/Java class use (map list to string)
*/
fun <T : Any> mapListToJson(data: Map<String, List<T>>, type: Class<T>): String? {
    return try {
        GsonBuilder().setPrettyPrinting().create().toJson(data, getTypeList(type))
    } catch (e: JsonParseException) {
        String.EMPTY
    }
}

fun <T : Any> toJson(data: Map<String, List<T>>): String? {
    return try {
        GsonBuilder().setPrettyPrinting().create().toJson(data)
    } catch (e: JsonParseException) {
        String.EMPTY
    }
}

/*
  For Kotlin/Java class use (string to map list)
*/
fun <T : Any> fromJsonToMapList(json: String?, type: Class<T>): Map<String, List<T>> {
    return try {
        GsonBuilder().setPrettyPrinting().create().fromJson(json, getTypeStringMap(type))
    } catch (e: JsonParseException) {
        emptyMap()
    }
}

fun <T : Any> getTypeList(typeArgument: Class<T>): Type? {
    return TypeToken.getParameterized(List::class.java, typeArgument).type
}

fun <T : Any> getTypeStringMap(typeArgument: Class<T>): Type? {
    return TypeToken.getParameterized(HashMap::class.java,String::class.java,List::class.java).type
}

fun <T : Any> getTypeClass(typeArgument: Class<T>): Type? {
    return TypeToken.getParameterized(typeArgument, typeArgument).type
}