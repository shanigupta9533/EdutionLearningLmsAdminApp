package com.edutionLearning.core_ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


class GenericListAdapter<T>(
    context: Context,
    private val layoutResId: Int,
    private var items: List<T>,
    private val onBind: ((T, ViewDataBinding, position: Int) -> Unit)? = null,
    private val onFilter: ((T, filterText: String) -> Boolean)? = null
) : ArrayAdapter<T>(context, layoutResId) {

    private var originalItems: MutableList<T> = items.toMutableList()
    private val arrayFilter = ArrayFilter()

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): T? {
        return items.getOrNull(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rootView = convertView
        if (rootView == null) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater, layoutResId, parent,
                false
            )
            rootView = binding.root
            rootView.tag = ListDataBindingViewHolder<T>(binding)
        }
        val listViewHolder = rootView.tag as ListDataBindingViewHolder<T>
        listViewHolder.bind(item = items[position], position, onBind)
        return rootView
    }

    override fun getFilter(): Filter {
        return arrayFilter
    }

    private inner class ArrayFilter : Filter() {

        private val lock: Any = Any()

        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val results = FilterResults()
            if (prefix.isNullOrEmpty()) {
                synchronized(lock) {
                    val list: ArrayList<T> = ArrayList<T>(originalItems)
                    results.values = list
                    results.count = list.size
                }
            } else {
                val prefixString = prefix.toString()
                val values: MutableList<T> = originalItems
                val newValues = values.filter { onFilter?.invoke(it, prefixString) ?: true }
                results.values = newValues
                results.count = newValues.size
            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            items = if (results.values != null) {
                results.values as List<T>
            } else {
                emptyList()
            }
            if (results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}