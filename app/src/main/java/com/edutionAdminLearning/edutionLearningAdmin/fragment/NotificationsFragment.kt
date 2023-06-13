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
import com.edutionAdminLearning.edutionLearningAdmin.data.model.NotificationData
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentNotificationsBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.NotificationListAdapterFragmentBinding
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants.REFRESH_TIME
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.HomeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsFragment : ViewModelBindingFragment<FragmentNotificationsBinding, HomeDetailsViewModel>(
    FragmentNotificationsBinding::inflate
) {

    private val adapter by lazy { createLectureAdapter() }
    override val viewModel: HomeDetailsViewModel by viewModels()

    override fun FragmentNotificationsBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.notification_details)
        vm = viewModel
    }

    override fun FragmentNotificationsBinding.setViewModelBindingData() {
        lifecycleOwner = viewLifecycleOwner
        recyclerView.adapter = adapter

        viewModel.getNotification()
        toolbar.addIcon.setOnClickListener {
            findNavController().navigate(
                NotificationsFragmentDirections.goToNotificationInsert()
            )
        }

        viewLifecycleScope?.launch {
            viewModel.respondSuccess.collect {
                if (it) {
                    viewModel.getNotification()
                }
            }
        }

        viewLifecycleScope?.launch {
            viewModel.userNotification.collect {
                adapter.submitList(it)
            }
        }

        swipeLayout.setOnRefreshListener {
            viewLifecycleScope?.launch {
                viewModel.getNotification()
                delay(REFRESH_TIME)
                swipeLayout.isRefreshing = false
            }
        }

        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recyclerView.scrollToPosition(0)
            }
        })

        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun createLectureAdapter(): GenericRecyclerViewAdapter<NotificationData> {
        return GenericRecyclerViewAdapter(
            getViewLayout = { R.layout.notification_list_adapter_fragment },
            areItemsSame = ::sameCourseSlot,
            onBind = { data, viewDataBinding, position ->
                with(viewDataBinding as NotificationListAdapterFragmentBinding) {
                    dotIndicator.setOnClickListener {
                        dotIndicator.createShowPopup(data).show()
                    }
                }
            },
        )
    }

    private fun View.createShowPopup(data: NotificationData) = PopupMenu(requireContext(), this).apply {
        menu.add(Menu.NONE, 1, 1, context.getString(R.string.delete))

        setOnMenuItemClickListener {
            when (it.itemId) {
                1 -> {
                   viewModel.notificationDelete(data.id)
                }
            }
            false
        }

    }

    override fun onPause() {
        super.onPause()
        binding.swipeLayout.isRefreshing = false
    }

    private fun sameCourseSlot(old: NotificationData, new: NotificationData): Boolean {
        return old.id == new.id
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}