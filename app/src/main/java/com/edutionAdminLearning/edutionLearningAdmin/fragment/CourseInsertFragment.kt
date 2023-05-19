package com.edutionAdminLearning.edutionLearningAdmin.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentCourseInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.CourseDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseInsertFragment: ViewModelBindingFragment<FragmentCourseInsertBinding, CourseDetailsViewModel>(
    FragmentCourseInsertBinding::inflate
) {

    override val viewModel: CourseDetailsViewModel by viewModels()

    override fun FragmentCourseInsertBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.insert_course_details)
    }

    override fun FragmentCourseInsertBinding.setViewModelBindingData() {
        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}