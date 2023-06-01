package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.view.Menu
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.edutionAdminLearning.core_ui.adapter.GenericRecyclerViewAdapter
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.data.model.BannerData
import com.edutionAdminLearning.edutionLearningAdmin.databinding.BannersListAdapterLayoutBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentBannersDetailsBinding
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.REFRESH_TIME
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.HomeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val HOME_KEYWORDS = "home"

@AndroidEntryPoint
class BannersDetailsFragment : ViewModelBindingFragment<FragmentBannersDetailsBinding, HomeDetailsViewModel>(
    FragmentBannersDetailsBinding::inflate
) {

    private val adapter by lazy { createLectureAdapter() }
    override val viewModel: HomeDetailsViewModel by viewModels()

    override fun FragmentBannersDetailsBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.banners_details)
        vm = viewModel
    }

    override fun FragmentBannersDetailsBinding.setViewModelBindingData() {
        lifecycleOwner = viewLifecycleOwner
        viewModel.getBannersData(HOME_KEYWORDS)
        recyclerView.adapter = adapter
        toolbar.addIcon.setOnClickListener {
            findNavController().navigate(
                CoursesFragmentDirections.goToCoursesInsert()
            )
        }

        viewLifecycleScope?.launch {
            viewModel.respondSuccess.collect {
                if (it) {
                    viewModel.getBannersData(HOME_KEYWORDS)
                }
            }
        }

        viewLifecycleScope?.launch {
            viewModel.coursesBanners.collect {
                adapter.submitList(it)
            }
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recyclerView.scrollToPosition(0)
            }
        })

        swipeLayout.setOnRefreshListener {
            viewLifecycleScope?.launch {
                viewModel.getBannersData(HOME_KEYWORDS)
                delay(REFRESH_TIME)
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

    private fun createLectureAdapter(): GenericRecyclerViewAdapter<BannerData> {
        return GenericRecyclerViewAdapter(
            getViewLayout = { R.layout.banners_list_adapter_layout },
            areItemsSame = ::sameCourseSlot,
            onBind = { data, viewDataBinding, position ->
                with(viewDataBinding as BannersListAdapterLayoutBinding) {
                    bannerName.text = "Banner Number " + (position + 1)
                    dotIndicator.setOnClickListener {
                        dotIndicator.createShowPopup(data).show()
                    }
                }
            },
        )
    }

    private fun View.createShowPopup(data: BannerData) = PopupMenu(requireContext(), this).apply {
        menu.add(Menu.NONE, 0, 0, context.getString(R.string.edit_details))
        menu.add(Menu.NONE, 1, 1, context.getString(R.string.delete))

        setOnMenuItemClickListener {
            when (it.itemId) {
                0 -> {
                    findNavController().navigate(
                        BannersDetailsFragmentDirections.goToBannerInsert(
                            data = data,
                            isUpdate = true
                        )
                    )
                }

                1 -> {
                    viewModel.bannerDelete(data.id)
                }
            }
            false
        }

    }

    private fun sameCourseSlot(old: BannerData, new: BannerData): Boolean {
        return old.id == new.id
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}