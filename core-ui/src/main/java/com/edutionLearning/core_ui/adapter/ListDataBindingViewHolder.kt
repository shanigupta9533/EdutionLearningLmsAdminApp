package com.edutionLearning.core_ui.adapter

import androidx.databinding.ViewDataBinding
import com.edutionLearning.core_ui.BR

class ListDataBindingViewHolder<T>(private val binding: ViewDataBinding) {

    fun bind(
        item: T,
        position: Int,
        onBind: ((T, ViewDataBinding, position: Int) -> Unit)?,
    ) {
        binding.setVariable(com.edutionLearning.core_ui.BR.item, item)
        binding.executePendingBindings()
        if (onBind != null) {
            onBind(item, binding, position)
        }
    }
}
