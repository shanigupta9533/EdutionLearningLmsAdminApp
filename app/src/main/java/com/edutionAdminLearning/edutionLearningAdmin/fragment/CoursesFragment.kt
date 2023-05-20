package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.view.Menu
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.edutionAdminLearning.core_ui.adapter.GenericRecyclerViewAdapter
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.data.model.CoursesDetailsData
import com.edutionAdminLearning.edutionLearningAdmin.databinding.CourseListAdapterLayoutBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentCoursesBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.CourseDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoursesFragment : ViewModelBindingFragment<FragmentCoursesBinding, CourseDetailsViewModel>(
    FragmentCoursesBinding::inflate
) {

    private val adapter by lazy { createLectureAdapter() }
    override val viewModel: CourseDetailsViewModel by viewModels()

    override fun FragmentCoursesBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.courses_details)
    }

    override fun FragmentCoursesBinding.setViewModelBindingData() {
        recyclerView.adapter = adapter

        viewModel.getAllCourses()
        toolbar.addIcon.setOnClickListener {
            findNavController().navigate(
                CoursesFragmentDirections.goToCoursesInsert()
            )
        }

        viewLifecycleScope?.launch {
            viewModel.getAllCourses.collect {
                adapter.submitList(it)
            }
        }

        swipeLayout.setOnRefreshListener {
            viewLifecycleScope?.launch {
                delay(3000)
                swipeLayout.isRefreshing = false
            }
        }

        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun createLectureAdapter(): GenericRecyclerViewAdapter<CoursesDetailsData> {
        return GenericRecyclerViewAdapter(
            getViewLayout = { R.layout.course_list_adapter_layout },
            areItemsSame = ::sameCourseSlot,
            onBind = { data, viewDataBinding, position ->
                with(viewDataBinding as CourseListAdapterLayoutBinding) {
                    dotIndicator.setOnClickListener {
                        dotIndicator.createShowPopup(data).show()
                    }

                    editIcon.setOnClickListener {

                    }

                    deleteIcon.setOnClickListener {

                    }
                }
            },
        )
    }

    private fun View.createShowPopup(coursesDetailsData: CoursesDetailsData) = PopupMenu(requireContext(), this).apply {
        menu.add(Menu.NONE, 0, 0, "Video Details")
        menu.add(Menu.NONE, 1, 1, "Purchase Details")
        menu.add(Menu.NONE, 1, 1, "Delete Course")

        if (coursesDetailsData.isLive)
            menu.add(Menu.NONE, 2, 2, "Disable")
        else
            menu.add(Menu.NONE, 3, 3, "Enable")

        setOnMenuItemClickListener {
            when (it.itemId) {
                0 -> {
                    findNavController().navigate(
                        CoursesFragmentDirections.goToCoursesVideos()
                    )
                }

                1 -> {

                    findNavController().navigate(
                        CoursesFragmentDirections.goToCoursePurchased()
                    )

                }

                2 -> {

                }

                3 -> {

                }
            }
            false
        }

    }

    private fun sameCourseSlot(old: CoursesDetailsData, new: CoursesDetailsData): Boolean {
        return old.id == new.id
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}