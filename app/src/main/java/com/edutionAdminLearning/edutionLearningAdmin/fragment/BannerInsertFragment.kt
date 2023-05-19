package com.edutionAdminLearning.edutionLearningAdmin.fragment

import androidx.fragment.app.viewModels
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentBannerInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.HomeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BannerInsertFragment: ViewModelBindingFragment<FragmentBannerInsertBinding, HomeDetailsViewModel>(
    FragmentBannerInsertBinding::inflate
) {

    override fun FragmentBannerInsertBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.banner_details_insert)
    }

    override fun FragmentBannerInsertBinding.setViewModelBindingData() {
        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override val viewModel: HomeDetailsViewModel by viewModels()

    override fun onBackPressed(): Boolean {
        return false
    }

}