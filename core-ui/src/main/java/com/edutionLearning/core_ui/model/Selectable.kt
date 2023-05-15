package com.edutionLearning.core_ui.model
import kotlinx.android.parcel.IgnoredOnParcel

open class Selectable {
    @IgnoredOnParcel
    open var selected: Boolean = false
}