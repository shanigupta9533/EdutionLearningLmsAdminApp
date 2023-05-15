package com.edutionAdminLearning.core_ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapter(fm:FragmentManager,var fragmentList: List<FragmentData>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val fragList = fragmentList[position]
        return fragList.fragment.apply { arguments = fragList.bundle }
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        val fragList = fragmentList[position]
        return fragList.title
    }

}
