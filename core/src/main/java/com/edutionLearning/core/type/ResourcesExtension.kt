package com.edutionLearning.core.type

import android.content.Context
import android.content.res.Resources.getSystem

fun Context.resString(resId: Int): String? {
    return if (resId == 0) null else getString(resId)
}

val Int.dp: Int get() = (this / getSystem().displayMetrics.density).toInt()

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()

val Float.dp: Float get() = (this / getSystem().displayMetrics.density)

val Float.px: Float get() = (this * getSystem().displayMetrics.density)

