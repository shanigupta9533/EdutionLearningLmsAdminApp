package com.edutionAdminLearning.core_ui.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class DataBindingViewHolder<T>(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: T,
        onItemClick: ((View, T, position: Int) -> Unit)? = null,
        onBind: ((T, ViewDataBinding, position: Int) -> Unit)?,
        position: Int,
    ) {
        binding.setVariable(com.edutionAdminLearning.core_ui.BR.item, item)
        binding.executePendingBindings()
        if (onItemClick != null) {
            binding.root.setOnClickListener {
                onItemClick(it, item, position)
            }
        }
        if (onBind != null) {
            onBind(item, binding, position)
        }
    }

    fun bind(
        item: T,
        onItemClick: ((View, T) -> Unit)? = null,
        onBind: ((T, ViewDataBinding, position: Int) -> Unit)?,
        position: Int,
    ) {
        binding.setVariable(com.edutionAdminLearning.core_ui.BR.item, item)
        binding.executePendingBindings()
        if (onItemClick != null) {
            binding.root.setOnClickListener {
                onItemClick(it, item)
            }
        }
        if (onBind != null) {
            onBind(item, binding, position)
        }
    }
    /*jsdfkj jsdf*/
}
