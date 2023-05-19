package com.edutionAdminLearning.edutionLearningAdmin.fragment

import androidx.fragment.app.viewModels
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentCourseVideosInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.CourseDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseVideosInsertFragment : ViewModelBindingFragment<FragmentCourseVideosInsertBinding, CourseDetailsViewModel>(
    FragmentCourseVideosInsertBinding::inflate
) {

    override val viewModel: CourseDetailsViewModel by viewModels()

    override fun FragmentCourseVideosInsertBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.course_videos_insert)
    }

    override fun FragmentCourseVideosInsertBinding.setViewModelBindingData() {
        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}