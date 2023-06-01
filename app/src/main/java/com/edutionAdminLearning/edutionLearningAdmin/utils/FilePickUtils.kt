package com.edutionAdminLearning.edutionLearningAdmin.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import com.edutionAdminLearning.core_ui.binding.DOWNLOADING_FILE_LOCATION
import com.edutionAdminLearning.core_ui.binding.MAIN_FOLDER_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*

object FilePickUtils {

    private var pathPlusName: String? = null
    private var returnCursor: Cursor? = null
    private var `is`: InputStream? = null

    suspend fun getFile(mContext: Context, mUri: Uri, onGranted: (file: File) -> Unit, onError: (error: String) -> Unit) = withContext(Dispatchers.Default) {

            var file: File?
            var size = -1

            val context: Context = mContext

            file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                File(
                    context.getExternalFilesDir(null)
                        .toString() + "$MAIN_FOLDER_NAME$DOWNLOADING_FILE_LOCATION"
                )
            } else {
                File(Environment.getExternalStorageDirectory().absolutePath + "$MAIN_FOLDER_NAME$DOWNLOADING_FILE_LOCATION")
            }

            if (!file.exists()) {
                if (file.mkdirs()) {
                    Log.i("TAG", "getFile: folder is created")
                }
            }

            returnCursor = context.contentResolver.query(mUri, null, null, null, null)
            try {
                `is` = context.contentResolver.openInputStream(mUri)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            try {
                try {
                    if (returnCursor != null && returnCursor?.moveToFirst() == true) {
                        if (mUri.scheme != null) if (mUri.scheme == "content") {
                            val sizeIndex: Int? = returnCursor?.getColumnIndex(OpenableColumns.SIZE)
                            size = returnCursor?.getLong(sizeIndex ?: 0)?.toInt()!!
                        } else if (mUri.scheme == "file") {
                            val ff: File? = mUri.path?.let { File(it) }
                            size = ff?.length()?.toInt() ?: 0
                        }
                    }
                } finally {
                    if (returnCursor != null) returnCursor?.close()
                }
                pathPlusName = file.path + "/" + getFileName(mUri, mContext)
                file = File(file.path + "/" + getFileName(mUri, mContext))
                val bis = BufferedInputStream(`is`)
                val fos = FileOutputStream(file)
                val data = ByteArray(1024)
                var total: Long = 0
                var count: Int
                while (bis.read(data).also { count = it } != -1) {
                    total += count.toLong()
                    fos.write(data, 0, count)
                }
                fos.flush()
                fos.close()
                onGranted(file)

            } catch (e: IOException) {
                onError(e.message ?: "")
            }

    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri, context: Context): String {
        var result: String? = null
        if (uri.scheme != null) {
            if (uri.scheme == "content") {
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            assert(result != null)
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

}