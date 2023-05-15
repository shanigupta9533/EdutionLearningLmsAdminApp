package com.edutionAdminLearning.core_ui.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class DataBindingHFViewHolder<T>(private val binding: ViewDataBinding, val viewType: Int) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: T?,
        onItemClick: ((View, T?, viewType: Int) -> Unit)? = null,
        onBind: ((T?, ViewDataBinding, position: Int, viewType: Int) -> Unit)?,
        position: Int,
    ) {
        binding.setVariable(com.edutionAdminLearning.core_ui.BR.item, item)
        binding.executePendingBindings()
        if (onItemClick != null) {
            binding.root.setOnClickListener {
                onItemClick(it, item, viewType)
            }
        }
        if (onBind != null) {
            onBind(item, binding, position, viewType)
        }
    }
}
