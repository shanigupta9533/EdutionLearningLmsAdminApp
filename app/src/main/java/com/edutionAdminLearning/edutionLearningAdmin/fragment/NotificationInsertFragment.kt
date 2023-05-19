package com.edutionAdminLearning.edutionLearningAdmin.fragment

import androidx.fragment.app.viewModels
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentBannerInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentNotificationInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.HomeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationInsertFragment: ViewModelBindingFragment<FragmentNotificationInsertBinding, HomeDetailsViewModel>(
    FragmentNotificationInsertBinding::inflate
) {

    override fun FragmentNotificationInsertBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.notification_insert)
    }

    override fun FragmentNotificationInsertBinding.setViewModelBindingData() {
        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override val viewModel: HomeDetailsViewModel by viewModels()

    override fun onBackPressed(): Boolean {
        return false
    }

}