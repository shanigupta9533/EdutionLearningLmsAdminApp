package com.edutionLearning.core_ui.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

fun TabLayout.showIndicator(count: Int, max: Int = count) {
    removeAllTabs()
    val absCount = if (count > max) max else count
    for (i in 0 until absCount)
        addTab(newTab())
}

fun TabLayout.setupWithRecyclerView(recyclerView: RecyclerView) {
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val itemPosition: Int = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            getTabAt(itemPosition)?.select()
        }
    }
    recyclerView.addOnScrollListener(scrollListener)
    addOnTabSelectedListener(object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            recyclerView.smoothScrollToPosition(tab.position)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}

        override fun onTabReselected(tab: TabLayout.Tab?) {}
    })
}