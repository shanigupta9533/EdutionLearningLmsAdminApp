package com.edutionAdminLearning.edutionLearningAdmin.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.core_ui.adapter.GenericRecyclerViewAdapter
import com.edutionAdminLearning.core_ui.extensions.toastL
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingFragment
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseDetailsUpdateDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseSpecUpdateDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseSubmitSpecDto
import com.edutionAdminLearning.edutionLearningAdmin.data.model.PurchaseDetails
import com.edutionAdminLearning.edutionLearningAdmin.data.model.PurchasedSpecSubmit
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.PurchaseDetailsUpdate
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

    private val args by navArgs<CoursePurchaseInsertFragmentArgs>()
    override val viewModel: CourseDetailsViewModel by viewModels()
    private val adapter by lazy { createPurchaseInsertAdapter() }
    override fun FragmentCoursePurchaseInsertBinding.setViewBindingVariables() {
        toolbarText = getString(R.string.insert_course_purchase_details)
        vm = viewModel
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun FragmentCoursePurchaseInsertBinding.setViewModelBindingData() {
        lifecycleOwner = viewLifecycleOwner
        recyclerView.adapter = adapter
        toolbar.appCompatImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewLifecycleScope?.launch {
            viewModel.respondSuccess.collect {
                if (it && args.isUpdate)
                    toastL(getString(R.string.data_update_successfully))
                else if(args.isUpdate.not() && it) {
                    toastL(getString(R.string.data_insert_successfully))
                    courseType.setText(String.EMPTY)
                    coursePrice.setText(String.EMPTY)
                    adapter.submitList(
                        createPurchaseSpecList(args.data)
                    ) {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        if (args.isUpdate)
            args.data?.let {
                submit.text = getString(R.string.update_course_purchase_details)
                courseType.setText(it.courseType.value)
                coursePrice.setText(it.price.value)
            }

        adapter.submitList(createPurchaseSpecList(args.data))

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
                    Toast.makeText(requireContext(), getString(R.string.min_1_item_required), Toast.LENGTH_SHORT).show()
                }
            }
        }

        addNewSpec.setOnClickListener {
            val list = adapter.currentList.toMutableList()
            list.add(PurchasedSpecSubmit())
            adapter.submitList(list)
        }

        submit.setOnClickListener {

            courseType.error = null
            coursePrice.error = null

            when {

                courseType.text.toString().isEmpty() || courseType.text.toString().length < 3 -> {
                    courseType.error = getString(R.string.min_3_letter_required_in_course_type_field)
                    courseType.requestFocus()
                }

                coursePrice.text.toString().isEmpty() -> {
                    coursePrice.error = getString(R.string.course_price_is_required)
                    coursePrice.requestFocus()
                }

                adapter.currentList.any { it.specName.length < 3 } -> {
                    Toast.makeText(context, getString(R.string.min_3_letter_is_required_in_specification_name), Toast.LENGTH_SHORT).show()
                }

                else -> {

                    val insertList = ArrayList<PurchaseSubmitSpecDto>()
                    val updateList = ArrayList<PurchaseSpecUpdateDto>()
                    adapter.currentList.forEach { courseDetails ->
                        if (courseDetails.specName.isEmpty().not()) {
                            insertAndUpdate(
                                isUpdate = args.isUpdate,
                                insertList = insertList,
                                updateList = updateList,
                                courseDetails = courseDetails
                            )
                        }
                    }

                    if (args.isUpdate)

                        viewModel.updatePurchaseDetails(
                            PurchaseDetailsUpdate(
                                purchaseId = args.data?.id.value,
                                purchaseDetailsUpdateDto = PurchaseDetailsUpdateDto(
                                    courseId = args.courseId,
                                    courseType = courseType.text.toString(),
                                    price = coursePrice.text.toString(),
                                    purchaseSpecList = updateList.toMutableList(),
                                    courseTypeId = args.data?.id.value
                                )
                            )
                        )
                    else
                        viewModel.submitPurchaseDetails(
                            PurchaseSubmitDto(
                                courseId = args.courseId,
                                courseType = courseType.text.toString(),
                                price = coursePrice.text.toString(),
                                purchaseSpec = insertList.toMutableList()
                            )
                        )

                }

            }
        }
    }

    private fun insertAndUpdate(
        isUpdate: Boolean,
        insertList: ArrayList<PurchaseSubmitSpecDto>,
        updateList: ArrayList<PurchaseSpecUpdateDto>,
        courseDetails: PurchasedSpecSubmit
    ) {
        courseDetails.apply {
            if (isUpdate.not()) {
                insertList.add(
                    PurchaseSubmitSpecDto(
                        available = available,
                        specName = specName
                    )
                )
            } else {
                updateList.add(
                    PurchaseSpecUpdateDto(
                        available = available,
                        purchaseSpecId = purchaseSpecId ?: "0",
                        specName = specName
                    )
                )
            }
        }
    }

    private fun createPurchaseInsertAdapter(): GenericRecyclerViewAdapter<PurchasedSpecSubmit> {
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


    private fun sameCourseSlot(old: PurchasedSpecSubmit, new: PurchasedSpecSubmit): Boolean {
        return old == new
    }

    private fun createPurchaseSpecList(data: PurchaseDetails?) = data?.purchaseSpec.let {
        it?.map { spec ->
            PurchasedSpecSubmit(
                specName = spec.specName,
                available = spec.available,
                purchaseSpecId = spec.id
            )
        }
    } ?: listOf(
        PurchasedSpecSubmit(),
        PurchasedSpecSubmit()
    )

    override fun onBackPressed(): Boolean {
        return false
    }
}