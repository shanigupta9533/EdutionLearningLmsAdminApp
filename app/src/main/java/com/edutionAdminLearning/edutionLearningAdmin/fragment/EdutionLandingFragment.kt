package com.edutionAdminLearning.edutionLearningAdmin.fragment

import androidx.navigation.fragment.findNavController
import com.edutionAdminLearning.core_ui.fragment.ViewBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentEdutionLandingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EdutionLandingFragment : ViewBindingFragment<FragmentEdutionLandingBinding>(
    FragmentEdutionLandingBinding::inflate
) {
    override fun FragmentEdutionLandingBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.edution_landing_page)
    }

    override fun FragmentEdutionLandingBinding.setViewBindingData() {
    }

}