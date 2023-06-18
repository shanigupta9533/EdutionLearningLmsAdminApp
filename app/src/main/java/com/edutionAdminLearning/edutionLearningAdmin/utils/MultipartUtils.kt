package com.edutionAdminLearning.edutionLearningAdmin.utils

import android.content.Context
import com.edutionAdminLearning.core_ui.extensions.toastL
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.BANNER_IMAGE_KEY
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.BANNER_UPLOAD_MODULE_KEY
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.BANNER_UPLOAD_UPDATE_MODULE_KEY
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.CODE_LINK
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.COURSE_BASIC_DETAILS
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.COURSE_BASIC_PRICE
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.COURSE_DESC
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.COURSE_ID
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.COURSE_IMAGE_KEY
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.COURSE_NAME
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.COURSE_UPLOAD_MODULE_KEY
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.COURSE_UPLOAD_UPDATE_MODULE_KEY
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.COURSE_VIDEO_UPDATE_UPLOAD_MODULE_KEY
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.COURSE_VIDEO_UPLOAD_MODULE_KEY
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.DISPLAY_ORDER
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.FILE_DOCUMENT_UPLOAD_ID
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.HOME_WORK_LINK
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.KEYWORDS
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.LECTURE_NAME
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.MESSAGE_TEXT
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.NOTIFICATION_TITLE
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.POST_METHOD
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.PROJECT_LINK
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.VIDEO_EMBED_URL
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.VIDEO_LINK
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.VIDEO_LOCATE
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.VIDEO_NAME
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.VIDEO_UPLOAD_MODULE_KEY
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.VIDEO_URL
import com.edutionAdminLearning.edutionLearningAdmin.workManager.VIDEO_ID
import com.edutionAdminLearning.network.utils.FileUploaderService
import java.io.File

const val UPDATE_CONTAINS_URL = "?_method=PUT"
fun startUploading(
    notificationTitle: String = NOTIFICATION_TITLE,
    uploadId: String,
    fileUploaderService: FileUploaderService,
    parameterKey: List<FileUploaderService.MultipartKey>,
    fileArray: List<FileUploaderService.MultipartData>,
    moduleKey: String = FILE_DOCUMENT_UPLOAD_ID,
    endPointUrl: String,
    method: String = POST_METHOD
) {

    val multipartDataHolder = FileUploaderService.MultipartDataHolder(
        uniqueId = moduleKey + uploadId,
        method = method,
        fileArray = fileArray,
        notificationTitle = notificationTitle,
        parameterArray = parameterKey
    )

    fileUploaderService.multipartPartService(
        endPointUrl,
        multipartDataHolder,
    )

}

fun Context.setBannerUploadFile(
    data: File?,
    messageText: String,
    keywords: String,
    fileUploaderService: FileUploaderService,
) {

    if (data == null) {
        toastL(getString(R.string.something_went_wrong))
        return
    }

    val fileUrls = data.path
    if (fileUrls.isNullOrEmpty() || data.length() == 0L) {
        toastL(getString(R.string.something_went_wrong))
        return
    }

    if (!File(fileUrls).exists()) {
        toastL(getString(R.string.something_went_wront_please_verify_your))
        return
    }

    val parameterKey = listOf(
        FileUploaderService.MultipartKey(
            key = MESSAGE_TEXT,
            value = messageText
        ),

        FileUploaderService.MultipartKey(
            key = KEYWORDS,
            value = keywords
        )
    )

    val fileArray = listOf(
        FileUploaderService.MultipartData(
            key = BANNER_IMAGE_KEY,
            file = data
        )
    )

    startUploading(
        uploadId = uniqueId(),
        fileUploaderService = fileUploaderService,
        parameterKey = parameterKey,
        fileArray = fileArray,
        endPointUrl = EndPointUrl.BANNER_END_POINT.url,
        moduleKey = BANNER_UPLOAD_MODULE_KEY
    )

}

fun setVideoUploadFile(
    videoUrl: String,
    videoEmbedUrl: String,
    messageText: String,
    fileUploaderService: FileUploaderService,
) {

    val parameterKey = listOf(
        FileUploaderService.MultipartKey(
            key = VIDEO_NAME,
            value = messageText
        ),

        FileUploaderService.MultipartKey(
            key = VIDEO_URL,
            value = videoUrl
        ),

        FileUploaderService.MultipartKey(
            key = VIDEO_EMBED_URL,
            value = videoEmbedUrl
        ),

    )

    startUploading(
        uploadId = uniqueId(),
        fileUploaderService = fileUploaderService,
        parameterKey = parameterKey,
        fileArray = emptyList(),
        endPointUrl = EndPointUrl.VIDEO_END_POINT.url,
        moduleKey = VIDEO_UPLOAD_MODULE_KEY
    )

}

fun updateBannerUploadFile(
    bannerId: String,
    data: File?,
    messageText: String,
    keywords: String,
    fileUploaderService: FileUploaderService,
) {

    val parameterKey = listOf(

        FileUploaderService.MultipartKey(
            key = MESSAGE_TEXT,
            value = messageText
        ),

        FileUploaderService.MultipartKey(
            key = KEYWORDS,
            value = keywords
        )
    )

    val fileArray = data?.let {
        listOf(
            FileUploaderService.MultipartData(
                key = BANNER_IMAGE_KEY,
                file = data
            )
        )
    } ?: emptyList()

    startUploading(
        uploadId = uniqueId(),
        fileUploaderService = fileUploaderService,
        parameterKey = parameterKey,
        fileArray = fileArray,
        endPointUrl = EndPointUrl.BANNER_UPDATE_END_POINT.url + bannerId + UPDATE_CONTAINS_URL,
        moduleKey = BANNER_UPLOAD_UPDATE_MODULE_KEY
    )

}

fun Context.setCourseUploadFile(
    data: File?,
    courseName: String,
    courseDesc: String,
    coursePrice: String,
    fileUploaderService: FileUploaderService,
) {

    if (data == null) {
        toastL(getString(R.string.something_went_wrong))
        return
    }

    val fileUrls = data.path
    if (fileUrls.isNullOrEmpty() || data.length() == 0L) {
        toastL(getString(R.string.something_went_wrong))
        return
    }

    if (!File(fileUrls).exists()) {
        toastL(getString(R.string.something_went_wront_please_verify_your))
        return
    }

    val parameterKey = listOf(
        FileUploaderService.MultipartKey(
            key = COURSE_NAME,
            value = courseName
        ),

        FileUploaderService.MultipartKey(
            key = COURSE_DESC,
            value = courseDesc
        ),

        FileUploaderService.MultipartKey(
            key = COURSE_BASIC_PRICE,
            value = coursePrice
        )
    )

    val fileArray = listOf(
        FileUploaderService.MultipartData(
            key = COURSE_IMAGE_KEY,
            file = data
        )
    )

    startUploading(
        uploadId = uniqueId(),
        fileUploaderService = fileUploaderService,
        parameterKey = parameterKey,
        fileArray = fileArray,
        endPointUrl = EndPointUrl.COURSE_END_POINT.url,
        moduleKey = COURSE_UPLOAD_MODULE_KEY
    )

}

fun updateCourseUploadFile(
    courseId: String,
    data: File?,
    courseName: String,
    courseDesc: String,
    coursePrice: String,
    fileUploaderService: FileUploaderService,
) {

    val parameterKey = listOf(
        FileUploaderService.MultipartKey(
            key = COURSE_NAME,
            value = courseName
        ),

        FileUploaderService.MultipartKey(
            key = COURSE_DESC,
            value = courseDesc
        ),

        FileUploaderService.MultipartKey(
            key = COURSE_BASIC_DETAILS,
            value = coursePrice
        )
    )

    val fileArray = data?.let {
        listOf(
            FileUploaderService.MultipartData(
                key = COURSE_IMAGE_KEY,
                file = data
            )
        )
    } ?: emptyList()

    startUploading(
        uploadId = uniqueId(),
        fileUploaderService = fileUploaderService,
        parameterKey = parameterKey,
        fileArray = fileArray,
        endPointUrl = EndPointUrl.COURSE_UPDATE_END_POINT.url + courseId + UPDATE_CONTAINS_URL,
        moduleKey = COURSE_UPLOAD_UPDATE_MODULE_KEY
    )

}

fun setCourseVideoUploadFile(
    lectureName: String,
    codeFile: File?,
    homeWorkFile: File?,
    projectFile: File?,
    courseId: String,
    videoId: String,
    fileUploaderService: FileUploaderService,
) {

    val parameterKey = listOf(
        FileUploaderService.MultipartKey(
            key = LECTURE_NAME,
            value = lectureName
        ),

        FileUploaderService.MultipartKey(
            key = COURSE_ID,
            value = courseId
        ),

        FileUploaderService.MultipartKey(
            key = VIDEO_ID,
            value = videoId
        )
    )

    val arrayList = ArrayList<FileUploaderService.MultipartData>()
    if (codeFile != null && codeFile.exists()) {
        arrayList.add(
            FileUploaderService.MultipartData(
                key = CODE_LINK,
                file = codeFile
            )
        )
    }

    if (homeWorkFile != null && homeWorkFile.exists()) {
        arrayList.add(
            FileUploaderService.MultipartData(
                key = HOME_WORK_LINK,
                file = homeWorkFile
            )
        )
    }

    if (projectFile != null && projectFile.exists()) {
        arrayList.add(
            FileUploaderService.MultipartData(
                key = PROJECT_LINK,
                file = projectFile
            )
        )
    }

    startUploading(
        uploadId = uniqueId(),
        fileUploaderService = fileUploaderService,
        parameterKey = parameterKey,
        fileArray = arrayList,
        endPointUrl = EndPointUrl.COURSE_VIDEO_END_POINT.url,
        moduleKey = COURSE_VIDEO_UPLOAD_MODULE_KEY
    )

}

fun updateCourseVideoUploadFile(
    lectureName: String,
    codeFile: File?,
    homeWorkFile: File?,
    projectFile: File?,
    courseId: String,
    courseVideoId: String,
    videoId: String,
    displayNumber: String,
    fileUploaderService: FileUploaderService,
) {

    val parameterKey = listOf(
        FileUploaderService.MultipartKey(
            key = LECTURE_NAME,
            value = lectureName
        ),

        FileUploaderService.MultipartKey(
            key = COURSE_ID,
            value = courseId
        ),

        FileUploaderService.MultipartKey(
            key = DISPLAY_ORDER,
            value = displayNumber
        ),

        FileUploaderService.MultipartKey(
            key = VIDEO_ID,
            value = videoId
        )
    )

    val arrayList = ArrayList<FileUploaderService.MultipartData>()
    if (codeFile != null && codeFile.exists()) {
        arrayList.add(
            FileUploaderService.MultipartData(
                key = CODE_LINK,
                file = codeFile
            )
        )
    }

    if (homeWorkFile != null && homeWorkFile.exists()) {
        arrayList.add(
            FileUploaderService.MultipartData(
                key = HOME_WORK_LINK,
                file = homeWorkFile
            )
        )
    }

    if (projectFile != null && projectFile.exists()) {
        arrayList.add(
            FileUploaderService.MultipartData(
                key = PROJECT_LINK,
                file = projectFile
            )
        )
    }

    startUploading(
        uploadId = uniqueId(),
        fileUploaderService = fileUploaderService,
        parameterKey = parameterKey,
        fileArray = arrayList,
        endPointUrl = EndPointUrl.COURSE_VIDEO_UPDATE_END_POINT.url + courseVideoId + UPDATE_CONTAINS_URL,
        moduleKey = COURSE_VIDEO_UPDATE_UPLOAD_MODULE_KEY
    )

}