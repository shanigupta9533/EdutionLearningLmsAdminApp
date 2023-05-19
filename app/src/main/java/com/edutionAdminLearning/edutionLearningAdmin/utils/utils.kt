package com.edutionAdminLearning.edutionLearningAdmin.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import com.edutionAdminLearning.core_ui.extensions.toastL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
    SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
}

inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
    SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}

fun <T> SharedFlow<T>.getValueBlockedOrNull(): T? {
    var value: T?
    runBlocking(Dispatchers.Default) {
        value = when (this@getValueBlockedOrNull.replayCache.isEmpty()) {
            true -> null
            else -> this@getValueBlockedOrNull.firstOrNull()
        }
    }
    return value
}

fun Context.whatsAppLaunch(contact: String, shareText: String) {

    val contactNumber = if (contact.startsWith("+91")) contact
    else if (contact.startsWith("91") && contact.length == 12) "+$contact"
    else "+91$contact"

    val url = String.format("https://api.whatsapp.com/send?phone=%s&text=%s", contactNumber, shareText)

    try {
        val pm: PackageManager = packageManager
        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
        val i = Intent(Intent.ACTION_VIEW)
        i.putExtra(Intent.EXTRA_TEXT, shareText)
        i.data = Uri.parse(url)
        startActivity(i)
    } catch (e: PackageManager.NameNotFoundException) {
        whatsAppBusinessLaunch(contactNumber, shareText)
        e.printStackTrace()
    }

}

fun String.setStringTimeStampWithDate(): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val mDate: Date? = dateFormat.parse(this)
    return mDate?.time ?: 0
}


private fun Context.whatsAppBusinessLaunch(contact: String, shareText: String) {

    val url = String.format("https://api.whatsapp.com/send?phone=%s&text=%s", contact, shareText)
    try {
        val pm: PackageManager = packageManager
        pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES)
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    } catch (e: PackageManager.NameNotFoundException) {
        toastL("Whatsapp not installed..")
        e.printStackTrace()
    }
}