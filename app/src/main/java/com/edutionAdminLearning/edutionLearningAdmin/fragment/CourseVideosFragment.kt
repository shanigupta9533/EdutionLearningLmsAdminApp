package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.view.Menu
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import com.edutionAdminLearning.core_ui.adapter.GenericRecyclerViewAdapter
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.CourseListAdapterLayoutBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.CourseVideosListAdapterBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentCourseVideosBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.HomeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CourseVideosFragment : ViewModelBindingFragment<FragmentCourseVideosBinding, HomeDetailsViewModel>(
    FragmentCourseVideosBinding::inflate
) {

    private val adapter by lazy { createLectureAdapter() }
    override val viewModel: HomeDetailsViewModel by viewModels()

    override fun FragmentCourseVideosBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.course_videos)
    }

    override fun FragmentCourseVideosBinding.setViewModelBindingData() {
        recyclerView.adapter = adapter
        adapter.submitList((0..20).toList())

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

    private fun View.createShowPopup(data: Int) = PopupMenu(requireContext(), this).apply {
        menu.add(Menu.NONE, 0, 0, context.getString(R.string.edit_details))
        menu.add(Menu.NONE, 1, 1, context.getString(R.string.delete))

        setOnMenuItemClickListener {
            when (it.itemId) {
                0 -> {

                }
                1 -> {

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