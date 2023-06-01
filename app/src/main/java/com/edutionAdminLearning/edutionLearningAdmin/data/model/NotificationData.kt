package com.edutionAdminLearning.edutionLearningAdmin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationData(
    var id: String,
    var noticeText: String,
    var createdAt: String,
    var seen: Boolean
) : Parcelable