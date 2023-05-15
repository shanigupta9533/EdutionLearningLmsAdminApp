package com.edutionLearning.core_ui.binding

import androidx.databinding.BaseObservable

class ExpandableTextDetail(
    var titleText: String?=null,
    var detailText: String?=null,
    var expanded: Boolean
) : BaseObservable(){

    fun toggle() {
        expanded = !expanded
        notifyChange()
    }

}