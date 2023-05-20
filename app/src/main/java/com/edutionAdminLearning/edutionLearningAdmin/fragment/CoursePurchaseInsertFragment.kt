package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.util.Log
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core_ui.adapter.GenericRecyclerViewAdapter
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.data.model.PurchasedSpecSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.databinding.CoursePurchaseInsertListAdapterBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.FragmentCoursePurchaseInsertBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.CourseDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class CoursePurchaseInsertFragment : ViewModelBindingFragment<FragmentCoursePurchaseInsertBinding, CourseDetailsViewModel>(
    FragmentCoursePurchaseInsertBinding::inflate
) {

    override val viewModel: CourseDetailsViewModel by viewModels()
    private val adapter by lazy { createPurchaseInsertAdapter() }
    override fun FragmentCoursePurchaseInsertBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.insert_course_purchase_details)
    }

    override fun FragmentCoursePurchaseInsertBinding.setViewModelBindingData() {
        recyclerView.adapter = adapter
        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        adapter.submitList(
            createPurchaseSpecList()
        )

        viewLifecycleScope?.launch {
            viewModel.deletePurchaseSpec.collect { position ->
                val list = adapter.currentList.toMutableList()
                if (list.size > 1) {
                    try {
                        list.removeAt(position)
                        recyclerView.removeViewAt(position)
                    } catch (e: Exception) {
                        Log.i("TAG", "setViewModelBindingData12345: " + e.printStackTrace())
                    }
                    adapter.submitList(list)
                } else {
                    Toast.makeText(requireContext(), "Min 1 item required...", Toast.LENGTH_SHORT).show()
                }
            }
        }

        addNewSpec.setOnClickListener {
            val list = adapter.currentList.toMutableList()
            list.add(
                PurchasedSpecSubmitDto(
                    specName = String.EMPTY,
                    available = false
                )
            )
            adapter.submitList(list)
        }

        submit.setOnClickListener {
            if (adapter.currentList.none { it.specName.isEmpty().not() }){

            } else {
                adapter.currentList.forEach { courseDetails ->
                    if (courseDetails.specName.isEmpty().not()) {

                    }
                }
            }
        }
    }

    private fun createPurchaseInsertAdapter(): GenericRecyclerViewAdapter<PurchasedSpecSubmitDto> {
        return GenericRecyclerViewAdapter(
            getViewLayout = { R.layout.course_purchase_insert_list_adapter },
            areItemsSame = ::sameCourseSlot,
            onBind = { data, viewDataBinding, position ->
                with(viewDataBinding as CoursePurchaseInsertListAdapterBinding) {
                    courseSpec.doOnTextChanged { text, _, _, _ ->
                        data.specName = text.toString()
                    }
                    availability.setOnClickListener {
                        data.available = availability.isChecked
                    }
                    delete.setOnClickListener {
                        viewModel.deletePurchaseSpec(position)
                    }
                }
            },
        )
    }


    private fun sameCourseSlot(old: PurchasedSpecSubmitDto, new: PurchasedSpecSubmitDto): Boolean {
        return old == new
    }

    private fun createPurchaseSpecList() = listOf(

        PurchasedSpecSubmitDto(
            specName = String.EMPTY,
            available = false
        ),

        PurchasedSpecSubmitDto(
            specName = String.EMPTY,
            available = false
        )

    )

    override fun onBackPressed(): Boolean {
        return false
    }
}