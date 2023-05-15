package com.edutionAdminLearning.core_ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabViewAdapter(fragmentActivity: FragmentActivity, var fragmentList: List<FragmentData>) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        val fragList = fragmentList[position]
        return fragList.fragment.apply { arguments = fragList.bundle }
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    fun setTabLayoutMediator(tabLayout: TabLayout, viewPager: ViewPager2) {
        TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
            tab.text = fragmentList[position].title
        }.attach()
    }
}

data class FragmentData(
    var fragment: Fragment, var title: String, var bundle: Bundle? = null
)