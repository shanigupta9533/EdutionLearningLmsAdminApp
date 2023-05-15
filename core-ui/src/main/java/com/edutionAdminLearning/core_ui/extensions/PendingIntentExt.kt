package com.edutionAdminLearning.core_ui.extensions

import android.app.PendingIntent
import android.os.Build


object PendingIntentExt {
    @JvmField
    val FLAG_IMMUTABLE_COMPAT = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_IMMUTABLE
    } else PendingIntent.FLAG_UPDATE_CURRENT
}