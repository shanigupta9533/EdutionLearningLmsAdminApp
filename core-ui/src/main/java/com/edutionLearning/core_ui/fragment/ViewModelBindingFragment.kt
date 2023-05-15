package com.edutionLearning.core_ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.edutionLearning.core_ui.view.dialog.handleCompleteUiApiError
import com.edutionLearning.core_ui.view.dialog.handleSuccessDialog
import com.edutionLearning.core_ui.view.dialog.handleUiApiError
import com.edutionLearning.core_ui.viewmodel.BaseViewModel

/**
 * Use this fragment class as base class if you have to deal with view model and data binding in fragment.
 * While extending this class follow the below mentioned coding pattern

class PathshalaFragment: ViewModelBindingFragment<FragmentPathshalaBinding, PathshalaViewModel>(
FragmentPathshalaBinding::inflate,
{ ViewModelProvider(it).get(PathshalaViewModel::class.java) }
)

 *  in above example ChildViewModel is view model class and FragmentChildBinding is a generated data binding class of fragment_child.xml layout.
 */
abstract class ViewModelBindingFragment<VB : ViewBinding, VM : BaseViewModel>(
    bindingFactory: (LayoutInflater) -> VB,
) : ViewBindingFragment<VB>(bindingFactory) {

    /**
     * This variable contains the view model instance for this fragment.
     */
    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle API error
        handleUiApiError(viewModel)
        handleCompleteUiApiError(viewModel)
        // Success dialog
        handleSuccessDialog(viewModel)

        binding.setViewModelBindingData()

    }

    override fun VB.setViewBindingData() {

    }

    /**
     * Use this method to assign/set view model data to view binding
     */
    abstract fun VB.setViewModelBindingData()
}
