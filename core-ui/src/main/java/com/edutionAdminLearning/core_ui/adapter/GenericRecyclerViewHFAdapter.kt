package com.edutionAdminLearning.core_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class GenericRecyclerViewHFAdapter<T>(
    val getViewLayout: (viewType: Int) -> Int,
    areItemsSame: (oldItem: T, newItem: T) -> Boolean,
    areItemContentsEqual: (oldItem: T, newItem: T) -> Boolean = { old, new -> old == new },
    private val showHeader: Boolean = false,
    private val showFooter: Boolean = true,
    private val onItemClick: ((View, T?, viewType: Int) -> Unit)? = null,
    private val onBind: ((T?, ViewDataBinding, position: Int, viewType: Int) -> Unit)? = null,
) : ListAdapter<T, DataBindingHFViewHolder<T>>(DiffCallback(areItemsSame, areItemContentsEqual)) {

    constructor(
        getViewLayout: (viewType: Int) -> Int,
        areItemsSame: (oldItem: T, newItem: T) -> Boolean,
        areItemContentsEqual: (oldItem: T, newItem: T) -> Boolean = { old, new -> old == new },
        showHeader: Boolean = false,
        showFooter: Boolean = false,
        onItemClick: OnItemClickListener<T>,
        onBind: OnBindListener<T>,
    ) : this(
        getViewLayout,
        areItemsSame,
        areItemContentsEqual,
        showHeader,
        showFooter,
        onItemClick::onItemClick,
        onBind::onBind,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingHFViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, getViewLayout(viewType), parent,
            false
        )
//        Log.d("RecyclerViewHFAdapter", "viewType: $viewType")
        return DataBindingHFViewHolder(binding, viewType)
    }

    override fun getItemViewType(position: Int): Int {
//        Log.d("RecyclerViewHFAdapter", "position: $position, itemCount: $itemCount")
        return when {
            position == 0 && showHeader -> VIEW_TYPE_HEADER
            position == itemCount - 1 && showFooter -> VIEW_TYPE_FOOTER
            else -> VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        val actualItemCount = super.getItemCount()
        return if (actualItemCount == 0) 0
        else actualItemCount + when {
            showHeader && showFooter -> 2
            showFooter || showFooter -> 1
            else -> 0
        }
    }

    interface OnItemClickListener<T> {
        fun onItemClick(view: View, item: T?, viewType: Int)
    }

    interface OnBindListener<T> {
        fun onBind(item: T?, binding: ViewDataBinding, position: Int, viewType: Int)
    }

    override fun onBindViewHolder(holder: DataBindingHFViewHolder<T>, position: Int) =
        holder.bind(
            if (holder.viewType == VIEW_TYPE_NORMAL) getItem(position) else null,
            onItemClick,
            onBind,
            position
        )

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

    companion object {
        const val VIEW_TYPE_NORMAL = 1
        const val VIEW_TYPE_HEADER = 2
        const val VIEW_TYPE_FOOTER = 3
    }
}
