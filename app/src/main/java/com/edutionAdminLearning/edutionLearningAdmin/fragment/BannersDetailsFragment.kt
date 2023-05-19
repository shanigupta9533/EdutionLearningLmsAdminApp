package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.view.Menu
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.edutionAdminLearning.core_ui.adapter.GenericRecyclerViewAdapter
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.CourseVideosListAdapterBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentBannersDetailsBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.HomeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BannersDetailsFragment : ViewModelBindingFragment<FragmentBannersDetailsBinding, HomeDetailsViewModel>(
    FragmentBannersDetailsBinding::inflate
) {

    private val adapter by lazy { createLectureAdapter() }
    override val viewModel: HomeDetailsViewModel by viewModels()

    override fun FragmentBannersDetailsBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.banners_details)
    }

    override fun FragmentBannersDetailsBinding.setViewModelBindingData() {
        recyclerView.adapter = adapter
        adapter.submitList((0..20).toList())

        swipeLayout.setOnRefreshListener {
            viewLifecycleScope?.launch {
                delay(3000)
                swipeLayout.isRefreshing = false
            }
        }

        toolbar.addIcon.setOnClickListener {
            findNavController().navigate(
                BannersDetailsFragmentDirections.goToBannerInsert()
            )
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