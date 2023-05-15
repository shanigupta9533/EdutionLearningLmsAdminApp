package com.edutionLearning.core_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.edutionLearning.core_ui.model.BaseViewItemType

class GenericRecyclerMultiViewAdapter<T : BaseViewItemType>(
    val getViewLayout: (item: T) -> Int,
    areItemsSame: (oldItem: T, newItem: T) -> Boolean,
    areItemContentsEqual: (oldItem: T, newItem: T) -> Boolean = { old, new -> old == new },
    private val onItemClick: ((View, T) -> Unit)? = null,
    private val onBind: ((T, ViewDataBinding, position: Int) -> Unit)? = null,
) : ListAdapter<T, DataBindingViewHolder<T>>(DiffCallback(areItemsSame, areItemContentsEqual)) {

    constructor(
        getViewLayout: (item: T) -> Int,
        areItemsSame: (oldItem: T, newItem: T) -> Boolean,
        areItemContentsEqual: (oldItem: T, newItem: T) -> Boolean = { old, new -> old == new },
        onItemClick: OnItemClickListener<T>,
        onBind: OnBindListener<T>,
    ) : this(
        getViewLayout, areItemsSame, areItemContentsEqual, onItemClick::onItemClick, onBind::onBind,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return try {
            getItem(position).getViewType()
        } catch (e: java.lang.Exception) {
            getViewLayout(getItem(position))
        }
    }

    interface OnItemClickListener<T> {
        fun onItemClick(view: View, item: T)
    }

    interface OnBindListener<T> {
        fun onBind(item: T, binding: ViewDataBinding, position: Int)
    }


    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) = holder.bind(getItem(position), onItemClick, onBind, position)

    class DiffCallback<T : BaseViewItemType>(
        val sameItems: (oldItem: T, newItem: T) -> Boolean,
        val sameItemContents: (oldItem: T, newItem: T) -> Boolean,
    ) : DiffUtil.ItemCallback<T>() {
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = sameItemContents(oldItem, newItem)
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = sameItems(oldItem, newItem)
    }
}

