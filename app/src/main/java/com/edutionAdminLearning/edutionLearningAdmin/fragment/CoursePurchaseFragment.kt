package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.edutionAdminLearning.core_ui.adapter.GenericRecyclerViewAdapter
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.data.model.PurchaseDetails
import com.edutionAdminLearning.edutionLearningAdmin.databinding.CoursePurchaseListAdapterBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentCoursePurchaseBinding
import com.edutionAdminLearning.edutionLearningAdmin.utils.Constants
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.CourseDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoursePurchaseFragment: ViewModelBindingFragment<FragmentCoursePurchaseBinding, CourseDetailsViewModel>(
    FragmentCoursePurchaseBinding::inflate
) {

    private val args by navArgs<CoursePurchaseFragmentArgs>()
    private val adapter by lazy { createLectureAdapter() }
    override val viewModel: CourseDetailsViewModel by viewModels()

    override fun FragmentCoursePurchaseBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.course_purchase_details)
        vm = viewModel
    }

    override fun FragmentCoursePurchaseBinding.setViewModelBindingData() {
        lifecycleOwner = viewLifecycleOwner
        recyclerView.adapter = adapter
        viewModel.getAllPurchaseDetails(args.courseId)
        toolbar.addIcon.setOnClickListener {
            findNavController().navigate(
                CoursesFragmentDirections.goToCoursesInsert()
            )
        }

        viewLifecycleScope?.launch {
            viewModel.respondSuccess.collect {
                if (it) {
                    viewModel.getAllPurchaseDetails(args.courseId)
                }
            }
        }

        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recyclerView.scrollToPosition(0)
            }
        })

        viewLifecycleScope?.launch {
            viewModel.getAllPurchaseDetails.collect {
                adapter.submitList(it)
            }
        }

        viewLifecycleScope?.launch {
            viewModel.errorDetails.collect {
                if(it?.isNotEmpty() == true) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    adapter.submitList(emptyList())
                }
            }
        }

        swipeLayout.setOnRefreshListener {
            viewLifecycleScope?.launch {
                viewModel.getAllPurchaseDetails(args.courseId)
                delay(Constants.REFRESH_TIME)
                swipeLayout.isRefreshing = false
            }
        }

        toolbar.addIcon.setOnClickListener {
            findNavController().navigate(
                CoursePurchaseFragmentDirections.goToCoursePurchasedInsert(
                    isUpdate = false,
                    courseId = args.courseId
                )
            )
        }

        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    override fun onPause() {
        super.onPause()
        binding.swipeLayout.isRefreshing = false
    }

    private fun createLectureAdapter(): GenericRecyclerViewAdapter<PurchaseDetails> {
        return GenericRecyclerViewAdapter(
            getViewLayout = { R.layout.course_purchase_list_adapter },
            areItemsSame = ::sameCourseSlot,
            onBind = { data, viewDataBinding, position ->
                with(viewDataBinding as CoursePurchaseListAdapterBinding) {
                    dotIndicator.setOnClickListener {
                        dotIndicator.createShowPopup(data).show()
                    }
                }
            },
        )
    }

    private fun View.createShowPopup(data: PurchaseDetails) = PopupMenu(requireContext(), this).apply {
        menu.add(Menu.NONE, 0, 0, context.getString(R.string.edit_details))
        menu.add(Menu.NONE, 1, 1, context.getString(R.string.delete))

        setOnMenuItemClickListener {
            when (it.itemId) {
                0 -> {
                    findNavController().navigate(
                        CoursePurchaseFragmentDirections.goToCoursePurchasedInsert(
                            isUpdate = true,
                            courseId = args.courseId,
                            data = data
                        )
                    )
                }
                1 -> {
                    viewModel.purchaseDetailsDelete(data.id)
                }
            }
            false
        }

    }

    private fun sameCourseSlot(old: PurchaseDetails, new: PurchaseDetails): Boolean {
        return old.id == new.id
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}