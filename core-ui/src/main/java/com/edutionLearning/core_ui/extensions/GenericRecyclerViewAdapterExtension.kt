package com.edutionLearning.core_ui.extensions

import com.edutionLearning.core_ui.adapter.GenericRecyclerViewAdapter
import com.edutionLearning.core_ui.model.Selectable

fun <T : Selectable> GenericRecyclerViewAdapter<T>.toggleSelectedSingle(current: T) {
    // Reset All
    currentList.onEachIndexed { index, slotsItem ->
        if (slotsItem.selected) {
            slotsItem.selected = false
            notifyItemChanged(index)
            return@onEachIndexed
        }
    }
    current.selected = !current.selected
    notifyItemChanged(currentList.indexOf(current))
}

fun <T : Selectable> GenericRecyclerViewAdapter<T>.toggleSelectedMulti(current: T) {
    current.selected = !current.selected
    notifyItemChanged(currentList.indexOf(current))
}

fun <T : Selectable> GenericRecyclerViewAdapter<T>.removeAllSelected() {
    currentList.onEachIndexed{ index, item ->
        if(item.selected){
            item.selected = false
            notifyItemChanged(index)
        }
    }

}