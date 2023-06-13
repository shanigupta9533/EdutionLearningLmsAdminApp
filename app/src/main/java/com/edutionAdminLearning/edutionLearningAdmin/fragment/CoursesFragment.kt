package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.annotation.SuppressLint
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
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
        vm = viewModel
    }

    @SuppressLint("SuspiciousIndentation")
    override fun FragmentCoursesBinding.setViewModelBindingData() {
        lifecycleOwner = viewLifecycleOwner
        recyclerView.adapter = adapter

        viewModel.getAllCourses()
        toolbar.addIcon.setOnClickListener {
            findNavController().navigate(
                CoursesFragmentDirections.goToCoursesInsert()
            )
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recyclerView.scrollToPosition(0)
            }
        })

        viewLifecycleScope?.launch {
            viewModel.respondSuccess.collect {
                if (it) {
                    viewModel.getAllCourses()
                }
            }
        }

        viewLifecycleScope?.launch {
            viewModel.getAllCourses.collect {
                adapter.submitList(it)
            }
        }

        swipeLayout.setOnRefreshListener {
            viewLifecycleScope?.launch {
                viewModel.getAllCourses()
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
                        findNavController().navigate(
                            CoursesFragmentDirections.goToCoursesInsert(
                                data = data,
                                isUpdate = true
                            )
                        )
                    }

                    deleteIcon.setOnClickListener {
                        viewModel.deleteCourseDetails(data.id)
                    }
                }
            },
        )
    }

    override fun onPause() {
        super.onPause()
        binding.swipeLayout.isRefreshing = false
    }

    private fun View.createShowPopup(coursesDetailsData: CoursesDetailsData) = PopupMenu(requireContext(), this).apply {
        menu.add(Menu.NONE, 0, 0, context.getString(R.string.courses_details_video))
        menu.add(Menu.NONE, 1, 1, context.getString(R.string.purchase_details))

        if (coursesDetailsData.isLive)
            menu.add(Menu.NONE, 2, 2, context.getString(R.string.disable))
        else
            menu.add(Menu.NONE, 3, 3, context.getString(R.string.enable))

        menu.add(Menu.NONE, 4, 4, context.getString(R.string.user_purchased))
        menu.add(Menu.NONE, 5, 5, context.getString(R.string.video_details))

        setOnMenuItemClickListener {
            when (it.itemId) {
                0 -> {
                    findNavController().navigate(
                        CoursesFragmentDirections.goToCoursesVideos(coursesDetailsData.id)
                    )
                }

                1 -> {

                    findNavController().navigate(
                        CoursesFragmentDirections.goToCoursePurchased(coursesDetailsData.id)
                    )

                }

                2 -> {
                    viewModel.updateCourseUpdateLive(coursesDetailsData.id)
                }

                3 -> {
                    viewModel.updateCourseUpdateLive(coursesDetailsData.id)
                }

                4 -> {

                }

                5 -> {
                    findNavController().navigate(
                        CoursesFragmentDirections.goToVideo()
                    )
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