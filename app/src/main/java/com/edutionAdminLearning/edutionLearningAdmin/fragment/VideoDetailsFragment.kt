package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.content.DialogInterface
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core_ui.adapter.GenericRecyclerViewAdapter
import com.edutionAdminLearning.core_ui.extensions.onTextChangeFlow
import com.edutionAdminLearning.core_ui.extensions.toastL
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.data.model.VideoData
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentVideoDetailsBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.VideoListAdapterLayoutBinding
import com.edutionAdminLearning.edutionLearningAdmin.di.ChatEvents
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants
import com.edutionAdminLearning.edutionLearningAdmin.utils.createDialog
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.CourseDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideoDetailsFragment : ViewModelBindingFragment<FragmentVideoDetailsBinding, CourseDetailsViewModel>(
    FragmentVideoDetailsBinding::inflate
) {

    private val args by navArgs<VideoDetailsFragmentArgs>()
    private var isFirstTime = true
    private val adapter by lazy { createLectureAdapter() }
    override val viewModel: CourseDetailsViewModel by viewModels()

    override fun FragmentVideoDetailsBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.video_details)
        vm = viewModel
    }

    override fun FragmentVideoDetailsBinding.setViewModelBindingData() {
        lifecycleOwner = viewLifecycleOwner
        viewModel.getVideoDetails(String.EMPTY)
        recyclerView.adapter = adapter
        toolbar.addIcon.setOnClickListener {
            findNavController().navigate(
                CoursesFragmentDirections.goToCoursesInsert()
            )
        }

        if (args.isFromCourse)
            toolbar.addIcon.visibility = View.GONE

        viewLifecycleScope?.launch {
            viewModel.respondSuccess.collect {
                if (it) {
                    viewModel.getVideoDetails(String.EMPTY)
                }
            }
        }

        viewLifecycleScope?.launch {
            viewModel.videoDetails.collect {
                isFirstTime = false
                adapter.submitList(it)
            }
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recyclerView.scrollToPosition(0)
            }
        })

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            etSearch.onTextChangeFlow().debounce(500).filter { query ->
                return@filter isFirstTime.not()
            }.distinctUntilChanged().collect {
                viewModel.getVideoDetails(it)
            }
        }

        swipeLayout.setOnRefreshListener {
            viewLifecycleScope?.launch {
                viewModel.getVideoDetails(String.EMPTY)
                delay(Constants.REFRESH_TIME)
                swipeLayout.isRefreshing = false
            }
        }

        toolbar.addIcon.setOnClickListener {
            findNavController().navigate(
                VideoDetailsFragmentDirections.goToVideoInsert()
            )
        }

        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun createLectureAdapter(): GenericRecyclerViewAdapter<VideoData> {
        return GenericRecyclerViewAdapter(
            getViewLayout = { R.layout.video_list_adapter_layout },
            areItemsSame = ::sameCourseSlot,
            onBind = { data, viewDataBinding, position ->
                with(viewDataBinding as VideoListAdapterLayoutBinding) {
                    videoName.text = data.videoName

                    if (args.isFromCourse)
                        dotIndicator.visibility = View.GONE

                    dotIndicator.setOnClickListener {
                        dotIndicator.createShowPopup(data).show()
                    }

                    lectureItem.setOnClickListener {

                        if (args.isFromCourse) {
                            if (data.status) {
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                                ChatEvents.onVideoSelected(data)
                            } else {
                                toastL("Image is not parsing successfully...")
                            }
                        }
                    }
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()
        binding.swipeLayout.isRefreshing = false
    }

    private fun View.createShowPopup(data: VideoData) = PopupMenu(requireContext(), this).apply {
        menu.add(Menu.NONE, 1, 1, context.getString(R.string.delete))

        if(data.failed)
            menu.add(Menu.NONE, 2, 2, context.getString(R.string.retry))

        setOnMenuItemClickListener {
            when (it.itemId) {
                1 -> {
                    createDialog(
                       context = requireContext(),
                        title = context.getString(R.string.delete),
                        message = context.getString(R.string.are_you_sure_you_want_to_delete),
                        posClick = { _, _ ->
                            viewModel.videoDetailsDelete(data.id)
                        },
                        negClick = null
                    )
                }
                2 -> {
                    viewModel.retryVideos(data.id)
                }
            }
            false
        }

    }

    private fun sameCourseSlot(old: VideoData, new: VideoData): Boolean {
        return old.id == new.id
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}