package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.view.Menu
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.edutionAdminLearning.core_ui.adapter.GenericRecyclerViewAdapter
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.CourseListAdapterLayoutBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentCoursesBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.HomeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoursesFragment : ViewModelBindingFragment<FragmentCoursesBinding, HomeDetailsViewModel>(
    FragmentCoursesBinding::inflate
) {

    private val adapter by lazy { createLectureAdapter() }
    override val viewModel: HomeDetailsViewModel by viewModels()

    override fun FragmentCoursesBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.courses_details)
    }

    override fun FragmentCoursesBinding.setViewModelBindingData() {
        recyclerView.adapter = adapter
        adapter.submitList((0..20).toList())

        toolbar.addIcon.setOnClickListener {
            findNavController().navigate(
                CoursesFragmentDirections.goToCoursesInsert()
            )
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

    private fun createLectureAdapter(): GenericRecyclerViewAdapter<Int> {
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

    private fun View.createShowPopup(data: Int) = PopupMenu(requireContext(), this).apply {
        menu.add(Menu.NONE, 0, 0, "Video Details")
        menu.add(Menu.NONE, 1, 1, "Purchase Details")
        menu.add(Menu.NONE, 1, 1, "Delete Course")
        menu.add(Menu.NONE, 2, 2, "Disable")
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

    private fun sameCourseSlot(old: Int, new: Int): Boolean {
        return old == new
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}