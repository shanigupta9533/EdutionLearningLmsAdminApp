package com.edutionAdminLearning.edutionLearningAdmin.utils

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core_ui.extensions.toastL
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

val IMAGE_EXTENSIONS = arrayOf("image/jpg", "image/png", "image/gif", "image/jpeg", "image/webp")

val VIDEO_EXTENSIONS = arrayOf("video/mp4", "video/webm", "video/ogg", "video/avi")

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

fun uniqueId(): String {
    return UUID.randomUUID().toString().replace("-", String.EMPTY).uppercase(Locale.ROOT)
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

fun Context.makeStoragePermission(
    mimetypes: Array<String>, startLaunch: ActivityResultLauncher<Intent>
) {
    checkStoragePermission({
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        startLaunch.launch(intent)
    }) {

    }
}

fun Context.checkStoragePermission(onGranted: () -> Unit, onPermissionDenied: () -> Unit) {
    val tedPermission = TedPermission.create().setPermissionListener(object : PermissionListener {
        override fun onPermissionGranted() {
            onGranted()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            onPermissionDenied()
        }
    }).setDeniedMessage(getString(R.string.permission_denied_message))

    return if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        tedPermission.setPermissions(
            Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_AUDIO
        ).check()
    } else if (SDK_INT >= Build.VERSION_CODES.Q) {
        tedPermission.setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).check()
    } else {
        tedPermission.setPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).check()
    }

}

fun Context.checkNotificationPermission(onGranted: () -> Unit, onPermissionDenied: () -> Unit) {
    val tedPermission = TedPermission.create().setPermissionListener(object : PermissionListener {
        override fun onPermissionGranted() {
            onGranted()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            onPermissionDenied()
        }
    }).setDeniedMessage(getString(R.string.permission_denied_message))

    tedPermission.setPermissions(
        Manifest.permission.POST_NOTIFICATIONS
    ).check()

}

internal val mimeTypesOfPdf = arrayOf(
    "application/pdf",
)

internal val mimeTypesOfEverything = arrayOf(
    "*/*",
)

internal val mimeTypesOfVideo = arrayOf(
    "video/mp4",
    "video/webm",
    "video/mpeg"
)

fun createDialog(context: Context?, title: String?, message: String?, posBtnName: String? = "Yes", posClick: DialogInterface.OnClickListener?, negBtnName: String? = "No", negClick: DialogInterface.OnClickListener?) {
    val builder = AlertDialog.Builder(context!!)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(posBtnName, posClick)
    builder.setNegativeButton(negBtnName, negClick)
    val dialog = builder.create()
    dialog.show()
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