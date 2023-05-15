package com.penpencil.core.ui.extensions

import android.widget.EditText
import com.edutionAdminLearning.core.type.resString
import com.edutionAdminLearning.core.type.validation.ValidationResult
import com.edutionAdminLearning.core_ui.extensions.toastS

fun String?.isValidMobile(
    requiredMsg: String? = null,
    wrongMobileMsg: String? = null
): ValidationResult = when {
    isNullOrEmpty() -> ValidationResult(false, requiredMsg)
    length != 10 -> ValidationResult(false, wrongMobileMsg)
    else -> ValidationResult(true, requiredMsg)
}

fun String?.isValidText(
    requiredMsg: String? = null
): ValidationResult = when {
    isNullOrEmpty() -> ValidationResult(false, requiredMsg)
    else -> ValidationResult(true, requiredMsg)
}

fun EditText.isValidMobile(
    showMsg: Boolean = true,
    requiredMsg: Int = 0,
    wrongMobileMsg: Int = 0
) = with(
    text.toString().isValidMobile(context.resString(requiredMsg), context.resString(wrongMobileMsg))
) {
    if (notValid) {
        if (showMsg)
            context.toastS(errorMsg)
        false
    } else true
}

fun EditText.isValidText(
    showMsg: Boolean = true,
    requiredMsg: Int = 0
) = with(text.toString().isValidText(context.resString(requiredMsg))) {
    if (notValid) {
        if (showMsg)
            context.toastS(errorMsg)
        false
    } else true
}

fun EditText.isQueryLengthValid():Boolean  {
    val words: List<String> = text.split(" ")
    return  if(words.size>2)  true else false
}

