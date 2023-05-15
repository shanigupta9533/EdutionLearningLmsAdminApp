package com.edutionLearning.core_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

class GenericRecyclerViewPagingDataAdapter<T : Any>(
    val getViewLayout: (item: T) -> Int,
    areItemsSame: (oldItem: T, newItem: T) -> Boolean,
    areItemContentsEqual: (oldItem: T, newItem: T) -> Boolean = { old, new -> old == new },
    private val onItemClick: ((View, T, position: Int) -> Unit)? = null,
    private val onBind: ((T, ViewDataBinding, position: Int) -> Unit)? = null,
) : PagingDataAdapter<T, DataBindingViewHolder<T>>(
    DiffCallback(
        areItemsSame,
        areItemContentsEqual
    )
) {

    constructor(
        getViewLayout: (item: T) -> Int,
        areItemsSame: (oldItem: T, newItem: T) -> Boolean,
        areItemContentsEqual: (oldItem: T, newItem: T) -> Boolean = { old, new -> old == new },
        onItemClick: OnItemClickListener<T>,
        onBind: OnBindListener<T>,
    ) : this(
        getViewLayout, areItemsSame, areItemContentsEqual, onItemClick::onItemClick,
        onBind::onBind,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, viewType, parent,
            false
        )
        return DataBindingViewHolder(binding)
    }

    override fun getItemViewType(position: Int) =
        getItem(position)?.let { getViewLayout(it) } ?: 0 // Handle placeholder in case of type == 0

    interface OnItemClickListener<T> {
        fun onItemClick(view: View, item: T, position: Int)
    }

    interface OnBindListener<T> {
        fun onBind(item: T, binding: ViewDataBinding, position: Int)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        // Handle placeholder if view is null
        getItem(position)?.let {
            holder.bind(it, onItemClick, onBind, position)
        }
    }

    class DiffCallback<T>(
        val sameItems: (oldItem: T, newItem: T) -> Boolean,
        val sameItemContents: (oldItem: T, newItem: T) -> Boolean,
    ) : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) =
            sameItems(oldItem, newItem)

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return sameItemContents(oldItem, newItem)
        }
    }
}

