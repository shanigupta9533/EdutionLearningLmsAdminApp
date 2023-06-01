package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.edutionAdminLearning.core_ui.adapter.GenericRecyclerViewAdapter
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.data.model.CoursesVideo
import com.edutionAdminLearning.edutionLearningAdmin.databinding.CourseListAdapterLayoutBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.CourseVideosListAdapterBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentCourseVideosBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.CourseDetailsViewModel
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.HomeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CourseVideosFragment : ViewModelBindingFragment<FragmentCourseVideosBinding, CourseDetailsViewModel>(
    FragmentCourseVideosBinding::inflate
) {

    private val args by navArgs<CourseVideosFragmentArgs>()
    private val adapter by lazy { createLectureAdapter() }
    override val viewModel: CourseDetailsViewModel by viewModels()

    override fun FragmentCourseVideosBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.course_videos)
        vm = viewModel
    }

    override fun FragmentCourseVideosBinding.setViewModelBindingData() {
        lifecycleOwner = viewLifecycleOwner
        viewModel.getVideoDetails(args.courseId)
        recyclerView.adapter = adapter
        toolbar.addIcon.setOnClickListener {
            findNavController().navigate(
                CoursesFragmentDirections.goToCoursesInsert()
            )
        }

        viewLifecycleScope?.launch {
            viewModel.respondSuccess.collect {
                if (it) {
                    viewModel.getVideoDetails(args.courseId)
                }
            }
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recyclerView.scrollToPosition(0)
            }
        })

        viewLifecycleScope?.launch {
            viewModel.coursesVideoPlayer.collect {
                adapter.submitList(it)
            }
        }

        viewLifecycleScope?.launch {
            viewModel.errorDetails.collect {
                if(it?.isNotEmpty() == true)
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        swipeLayout.setOnRefreshListener {
            viewLifecycleScope?.launch {
                viewModel.getVideoDetails(args.courseId)
                delay(3000)
                swipeLayout.isRefreshing = false
            }
        }

        toolbar.addIcon.setOnClickListener {
            findNavController().navigate(
                CourseVideosFragmentDirections.goToCourseVideosInsert(
                    courseId = args.courseId
                )
            )
        }

        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun createLectureAdapter(): GenericRecyclerViewAdapter<CoursesVideo> {
        return GenericRecyclerViewAdapter(
            getViewLayout = { R.layout.course_videos_list_adapter },
            areItemsSame = ::sameCourseSlot,
            onBind = { data, viewDataBinding, position ->
                with(viewDataBinding as CourseVideosListAdapterBinding) {
                    dotIndicator.setOnClickListener {
                        dotIndicator.createShowPopup(data).show()
                    }
                }
            },
        )
    }

    private fun View.createShowPopup(courseVideo: CoursesVideo) = PopupMenu(requireContext(), this).apply {
        menu.add(Menu.NONE, 0, 0, context.getString(R.string.edit_details))
        menu.add(Menu.NONE, 1, 1, context.getString(R.string.delete))

        setOnMenuItemClickListener {
            when (it.itemId) {
                0 -> {
                    findNavController().navigate(
                        CourseVideosFragmentDirections.goToCourseVideosInsert(
                            data = courseVideo,
                            courseId = args.courseId,
                            isUpdate = true
                        )
                    )
                }
                1 -> {
                    viewModel.videoDetailsDelete(courseVideo.id)
                }
            }
            false
        }

    }

    private fun sameCourseSlot(old: CoursesVideo, new: CoursesVideo): Boolean {
        return old.id == new.id
    }

    override fun onPause() {
        super.onPause()
        binding.swipeLayout.isRefreshing = false
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}