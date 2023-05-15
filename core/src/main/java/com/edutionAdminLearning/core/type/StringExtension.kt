package com.edutionAdminLearning.core.type

import android.util.Patterns

val String.Companion.EMPTY: String by lazy { "" }
val String.Companion.COMMA: String by lazy { "," }

/**
 * This Extension field of type string returns the value of string or empty
 */
val String?.value : String get() = this ?: String.EMPTY

// This method is added for comparing contents of strings with ignoring case
fun String?.containsIgnoreCase(other: String?): Boolean {
    if (this==null || other == null) return false
    return this.contains(other, ignoreCase = true)
}

fun String?.checkIsNullOrEmpty(): Boolean{
    return this?.isEmpty() ?: true
}

fun String?.isContainsLink() : Boolean {
    if (this == null) return false
    return Patterns.WEB_URL.matcher(this.lowercase()).matches();
}
