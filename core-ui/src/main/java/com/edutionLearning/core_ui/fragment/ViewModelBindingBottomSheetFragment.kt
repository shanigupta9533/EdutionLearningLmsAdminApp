package com.edutionLearning.core_ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.edutionLearning.core_ui.viewmodel.BaseViewModel

/**
 * Use this fragment class as base class if you have to deal with view model and data binding in bottom sheet fragment.
 * While extending this class follow the below mentioned coding pattern

class PathshalaFragment: ViewModelBindingBottomSheetFragment<FragmentChildBinding, ChildViewModel>(
FragmentChildBinding::inflate,
{ ViewModelProvider(it).get(ChildViewModel::class.java) }
)

 *  in above example ChildViewModel is view model class and FragmentChildBinding is a generated data binding class of fragment_child.xml layout.
 */
abstract class ViewModelBindingBottomSheetFragment<VB : ViewBinding, VM : BaseViewModel>(
    bindingFactory: (LayoutInflater) -> VB,
) : ViewBindingBottomSheetFragment<VB>(bindingFactory) {

    /**
     * This variable contains the view model instance for this fragment.
     */
    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setViewModelBindingData()
    }

    override fun VB.setViewBindingData() {

    }

    /**
     * Use this method to assign/set view model data to view binding
     */
    abstract fun VB.setViewModelBindingData()
}
