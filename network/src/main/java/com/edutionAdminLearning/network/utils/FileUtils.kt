package com.edutionAdminLearning.network.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core.utils.CoroutineHelper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object FileUtils {


    private const val APP_NAME = "PhysicsWallah"
    private val coroutineHelper : CoroutineHelper = CoroutineHelper.create()

    @JvmStatic
    fun unzipAndDelete(zipFile: File?, targetDirectory: File) {
        try {
            coroutineHelper.launchOnIO {
                unzip(zipFile, targetDirectory)
                zipFile?.delete()
            }
        } catch (ignored: Exception) {
        }
    }

    @JvmStatic
    fun clearInternalStorage(context: Context) {
        coroutineHelper.launchOnIO {
            val file = File(getInternalStorageFile(context))
            deleteRecursively(file)
        }
    }

    @Throws(Exception::class)
    private fun unzip(zipFile: File?, targetDirectory: File) {

        if(zipFile == null)
            return

        if(!zipFile.exists())
            return

        val zis = ZipInputStream(
            BufferedInputStream(FileInputStream(zipFile))
        )
        try {
            var ze: ZipEntry
            var count: Int
            val buffer = ByteArray(8192)
            while (zis.nextEntry.also { ze = it } != null) {
                val file = File(targetDirectory, ze.name)
                val canonicalPath = file.canonicalPath
                if (targetDirectory.absolutePath.contains("..") && !canonicalPath.startsWith(
                        targetDirectory.absolutePath
                    )
                ) {
                    throw SecurityException("Something unexpected has happened")
                }
                val dir = if (ze.isDirectory) file else file.parentFile
                if (!dir.isDirectory && !dir.mkdirs()) throw FileNotFoundException(
                    "Failed to ensure directory: " +
                            dir.absolutePath
                )
                if (ze.isDirectory) continue
                val fout = FileOutputStream(file)
                try {
                    while (zis.read(buffer).also { count = it } != -1) fout.write(buffer, 0, count)
                } catch (ex: Exception) {
                    Log.e("UNZIP_", ex.toString())
                } finally {
                    fout.close()
                }
                /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } catch (e: Exception) {
            Log.e("UNZIP", e.toString())
        } finally {
            zis.close()
        }
    }

    fun getExternalStorageFile(context: Context): String {
        val file: File? = if (Build.VERSION.SDK_INT >= 30) {
            context.getExternalFilesDir(APP_NAME)
        } else {
            File(Environment.getExternalStorageDirectory().absolutePath + "/" + APP_NAME)
        }
        if (!file!!.exists()) {
            file.mkdirs()
        }
        return file.absolutePath
    }

    @JvmStatic
    fun getInternalStorageFile(context: Context): String {
        val file = File(context.filesDir.absolutePath + "/" + APP_NAME)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath
    }

    @JvmStatic
    fun getFile(directory: String?): File? {

        if(directory == null)
            return null

        val siteDirectory = File(directory)
        if (!siteDirectory.exists()) {
            siteDirectory.mkdirs()
        }
        return siteDirectory
    }

    @JvmStatic
    fun getFile(directory: String?, fileName: String?): File? {

        if(directory == null || fileName == null)
            return null

        val siteDirectory = File(directory)
        if (!siteDirectory.exists()) {
            siteDirectory.mkdirs()
        }
        return File(siteDirectory, fileName)
    }

    @JvmStatic
    fun deleteFile(directory: String?, fileName: String?): Boolean {

        if(directory == null || fileName == null)
            return true

        val siteDirectory = File(directory)
        if (!siteDirectory.exists()) {
            siteDirectory.mkdirs()
        }

        return File(siteDirectory, fileName).delete()
    }

    fun checkIfFileExist(filePath: String?): Boolean {

        if(filePath == null)
            return false

        val siteDirectory = File(filePath)
        return siteDirectory.exists()
    }

    fun getQualitiesOfDownloadedFiles(
        filePath: String?,
        fileName: String?,
        quality: Int
    ): List<String> {

        if(fileName == null || filePath == null){
            return ArrayList()
        }

        val siteDirectory = File(filePath)
        if (!siteDirectory.exists()) {
            return ArrayList()
        }
        val file = File(filePath, fileName)
        if (!file.exists()) {
            return ArrayList()
        }
        val downloadedFiles = file.listFiles() ?: return ArrayList()
        val filteredFiles: MutableList<String> = ArrayList()

        val mpdFilePath : String = getMPDFilePath(downloadedFiles,quality)
        if (mpdFilePath.isNotEmpty()) filteredFiles.add(mpdFilePath)

        return filteredFiles
    }

    private fun getMPDFilePath(files: Array<File>, quality: Int) : String {
        for (file in files) {
            if (file.isDirectory) {

                val matchingFiles: Array<File>? = file.listFiles { dir, name ->
                    name.contains(".mpd") && name.contains(
                        quality.toString()
                    )
                }

                matchingFiles?.firstOrNull {
                    return it.absolutePath
                }

                getMPDFilePath(file.listFiles() ?: emptyArray(), quality) // Calls same method again.
            } else if (file.name.contains(".mpd") && file.name.contains(quality.toString())){
                return file.absolutePath
            }
        }
        return String.EMPTY
    }

    @JvmStatic
    fun getZipExtensionForFile(videoId: String): String {
        return "$videoId.zip"
    }

    @JvmStatic
    fun getMP4ExtensionForFile(videoId: String): String {
        return "$videoId.mp4"
    }

    @JvmStatic
    fun getM3U8ExtensionForFile(videoId: String): String {
        return "$videoId.m3u8"
    }

    @JvmStatic
    fun getPDFExtensionForFile(fileId: String): String {
        return "$fileId.pdf"
    }

    @JvmStatic
    fun getPNGExtensionForFile(fileId: String): String {
        return "$fileId.png"
    }

    fun encodeToBase64(image: Bitmap): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun decodeBase64(input: String?): Bitmap {
        val decodedByte = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
    }

    @OptIn(DelicateCoroutinesApi::class)
    @JvmStatic
    fun deleteRecursively(fileOrDirectory: File?) {
        coroutineHelper.launchOnIO {
            if (fileOrDirectory == null) {
                return@launchOnIO
            }

            if (!fileOrDirectory.exists()) {
                return@launchOnIO
            }

            if (fileOrDirectory.isDirectory) {

                fileOrDirectory.listFiles()?.let {
                    for (child in it)
                        deleteRecursivelyInternal(child)
                }

                fileOrDirectory.delete()
            } else {
                fileOrDirectory.delete()
            }
            return@launchOnIO
        }
    }

    @JvmStatic
    private fun deleteRecursivelyInternal(fileOrDirectory: File) {

        if (!fileOrDirectory.exists()) {
            return
        }

        if (fileOrDirectory.isDirectory) {

            fileOrDirectory.listFiles()?.let {
                for (child in it)
                    deleteRecursivelyInternal(child)
            }
        }

        fileOrDirectory.delete()
    }

    @JvmStatic
    fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

}

fun getCorrectedFileName(filePath: String, dirPath: String): String {
    val split = filePath.split("/".toRegex()).toTypedArray()
    var pdfName: String
    val newFileName = StringBuilder()
    if (split.size >= 2) {
        pdfName = split[split.size - 1]
        pdfName = pdfName.replace(" ", "_")
        for (i in 0..split.size - 2) {
            newFileName.append(split[i]).append("/")
        }
        newFileName.append(pdfName)
    } else {
        newFileName.append(filePath.replace(" ", "_"))
    }
    val file = File("$dirPath/$newFileName")
    return if (!file.exists()) {
        newFileName.toString().replace(" ", "_")
    } else newFileName.toString()
}