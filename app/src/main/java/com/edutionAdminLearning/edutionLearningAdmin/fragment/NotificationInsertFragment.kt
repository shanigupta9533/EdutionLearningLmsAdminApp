package com.edutionAdminLearning.edutionLearningAdmin.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core_ui.extensions.toastL
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentBannerInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentNotificationInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.HomeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationInsertFragment : ViewModelBindingFragment<FragmentNotificationInsertBinding, HomeDetailsViewModel>(
    FragmentNotificationInsertBinding::inflate
) {

    override fun FragmentNotificationInsertBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.notification_insert)
        vm = viewModel
        controller = this@NotificationInsertFragment
    }

    override fun FragmentNotificationInsertBinding.setViewModelBindingData() {
        lifecycleOwner = viewLifecycleOwner
        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewLifecycleScope?.launch {
            viewModel.respondSuccess.collect{
                if(it){
                    notificationField.setText(String.EMPTY)
                }
            }
        }

    }

    override val viewModel: HomeDetailsViewModel by viewModels()

    override fun onBackPressed(): Boolean {
        return false
    }

    fun clickToAction() {

        binding.apply {

            notificationField.error = null
            when {
                notificationField.text.toString().length <= 3 -> {
                    notificationField.error = getString(R.string.min_3_letter_required_in_course_type_field)
                    notificationField.requestFocus()
                }
                else -> {
                    viewModel.notificationInsert(notificationField.text.toString())
                }
            }
        }
    }

}