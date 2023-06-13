package com.edutionAdminLearning.network.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import net.gotev.uploadservice.UploadService
import net.gotev.uploadservice.UploadServiceConfig
import net.gotev.uploadservice.data.UploadNotificationConfig
import net.gotev.uploadservice.data.UploadNotificationStatusConfig
import net.gotev.uploadservice.placeholders.Placeholder
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import java.io.File

const val AUTHORIZATION_TAG = "Authorization"
const val BEARER_TOKEN = "Bearer "
const val NOTIFICATION_FILE_SUCCESS = "File Upload Successful"
const val NOTIFICATION_FILE_SUCCESS_MESSAGE = ""
const val NOTIFICATION_FILE_ERROR = "File Upload Error"
const val NOTIFICATION_FILE_ERROR_MESSAGE = ""
const val NOTIFICATION_FILE_CANCELLED = "File Upload Cancelled"
const val NOTIFICATION_FILE_CANCELLED_MESSAGE = ""
const val CONTENT_TYPE = "UTF-8"

class FileUploaderService(
    private val context: Context,
    val baseUrl: String,
    private val authToken: () -> String
) {

    private fun uploadNotificationService(fileName: String): UploadNotificationConfig {

        return UploadNotificationConfig(
            notificationChannelId = UploadServiceConfig.defaultNotificationChannel!!,
            isRingToneEnabled = false,
            progress = UploadNotificationStatusConfig(
                title = fileName,
                message = "${Placeholder.Progress}",
                clearOnAction = true,
                iconResourceID = android.R.drawable.stat_sys_upload,
                autoClear = true

            ), success =
            UploadNotificationStatusConfig
                (
                title = NOTIFICATION_FILE_SUCCESS,
                message = NOTIFICATION_FILE_SUCCESS_MESSAGE,
                clearOnAction = true,
                autoClear = true
            ),

            error =
            UploadNotificationStatusConfig
                (
                title = NOTIFICATION_FILE_ERROR,
                message = NOTIFICATION_FILE_ERROR_MESSAGE,
                iconResourceID = android.R.drawable.stat_notify_error
            ),

            cancelled =
            UploadNotificationStatusConfig
                (
                title = NOTIFICATION_FILE_CANCELLED,
                message = NOTIFICATION_FILE_CANCELLED_MESSAGE,
                iconResourceID = android.R.drawable.stat_notify_error
            )
        )

    }

    companion object {

        @JvmStatic
        fun fileUriConverter(file: File, context: Context): Uri = FileProvider.getUriForFile(
            context.applicationContext,
            context.applicationContext.packageName + ".fileProvider",
            file
        )
    }

    fun multipartPartService(
        uploadUrl: String,
        multipartDataHolder: MultipartDataHolder,
    ) {

        val multipartUploadRequest = MultipartUploadRequest(context, baseUrl + uploadUrl)
            .setAutoDeleteFilesAfterSuccessfulUpload(true)
            .setMaxRetries(2)
            .addHeader("accept", "application/json")
            .addHeader(AUTHORIZATION_TAG, BEARER_TOKEN + authToken())
            .setUploadID(multipartDataHolder.uniqueId)
            .setMethod(multipartDataHolder.method)
            .setUsesFixedLengthStreamingMode(false)

        multipartDataHolder.parameterArray.forEach {
            multipartUploadRequest.addParameter(it.key, it.value)
        }

        multipartDataHolder.fileArray.forEach {
            multipartUploadRequest.addFileToUpload(
                it.file.path,
                it.key,
                it.file.name,
                CONTENT_TYPE
            )
        }

        multipartUploadRequest.startUpload()

    }

    fun stopService(channelId: String) = UploadService.stopUpload(channelId)
    fun getTaskList() = UploadService.taskList

    data class MultipartDataHolder(

        val uniqueId: String,
        val method: String,
        val notificationTitle: String,
        val fileArray: List<MultipartData> = emptyList(),
        val parameterArray: List<MultipartKey> = emptyList()

    )

    data class MultipartKey(
        val key: String,
        val value: String
    )

    data class MultipartData(
        val key: String,
        val file: File
    )

}