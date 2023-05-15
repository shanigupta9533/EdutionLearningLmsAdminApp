package com.edutionLearning.core_ui.fragment

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

//    val viewLifecycleScope by lazy { viewLifecycleOwner.lifecycleScope }

    open fun onBackPressed(): Boolean {
        return true
    }
}