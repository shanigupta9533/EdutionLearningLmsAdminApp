package com.edutionAdminLearning.edutionLearningAdmin.utils

object Constants {
    const val NOTIFICATION_CHANNEL_ID = "EDUTION DOCUMENT UPLOADS"
    const val NOTIFICATION_CHANNEL_NAME = "EDUTION CHAT DOCUMENTS"
    const val REFRESH_TIME = 3000L
    const val POST_METHOD = "POST"
    const val FILE_DOCUMENT_UPLOAD_ID = "file_document_upload_id"
    const val NOTIFICATION_TITLE = "File Is Uploading"
    const val MESSAGE_TEXT = "messageText"
    const val COURSE_NAME = "course_name"
    const val VIDEO_NAME = "video_name"
    const val LECTURE_NAME = "lecture_name"
    const val VIDEO_LINK = "video_link"
    const val HOME_WORK_LINK = "home_work_link"
    const val PROJECT_LINK = "project_link"
    const val CODE_LINK = "code_link"
    const val COURSE_ID = "course_id"
    const val DISPLAY_ORDER = "display_order"
    const val COURSE_DESC = "course_desc"
    const val COURSE_CODE_LINK = "code_link"
    const val COURSE_VIDEO_LINK = "course_video_link"
    const val COURSE_HOME_WORK_LINK = "course_home_work_link"
    const val COURSE_PROJECT_LINK = "course_project_link"
    const val COURSE_BASIC_PRICE = "course_basic_price"
    const val COURSE_BASIC_DETAILS = "course_price_details"
    const val KEYWORDS = "keywords"
    const val BANNER_IMAGE_KEY = "image"
    const val VIDEO_LOCATE = "video_locate"
    const val COURSE_IMAGE_KEY = "course_image"
    const val BANNER_UPLOAD_MODULE_KEY = "banner_upload_module_key_"
    const val BANNER_UPLOAD_UPDATE_MODULE_KEY = "banner_upload_update_module_key_"
    const val VIDEO_UPLOAD_MODULE_KEY = "video_upload_module_key_"
    const val COURSE_UPLOAD_MODULE_KEY = "course_upload_module_key_"
    const val COURSE_UPLOAD_UPDATE_MODULE_KEY = "course_upload_update_module_key_"
    const val COURSE_VIDEO_UPLOAD_MODULE_KEY = "course_video_upload_module_key_"
    const val COURSE_VIDEO_UPDATE_UPLOAD_MODULE_KEY = "course_video_update_upload_module_key_"
}

enum class EndPointUrl(var url: String){
    BANNER_END_POINT("api/admin/banner"),
    BANNER_UPDATE_END_POINT("api/admin/banner/"),
    COURSE_END_POINT("api/admin/courseDetails"),
    COURSE_UPDATE_END_POINT("api/admin/courseDetails/"),
    COURSE_VIDEO_END_POINT("api/admin/courseVideo"),
    COURSE_VIDEO_UPDATE_END_POINT("api/admin/courseVideo/"),
    VIDEO_END_POINT("api/admin/videos")
}