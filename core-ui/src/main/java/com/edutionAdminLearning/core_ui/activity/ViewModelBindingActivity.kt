package com.edutionAdminLearning.core_ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

/**
 * Use this activity class as base class if you have to deal with view model and data binding in activity.
 * While extending this class follow the below mentioned coding pattern

    class PathshalaActivity: ViewModelBindingActivity<ActivityPathshalaBinding, PathshalaViewModel>(
        ActivityPathshalaBinding::inflate,
        { ViewModelProvider(it).get(PathshalaViewModel::class.java) }
    )

 *  in above example ChildViewModel is view model class and ActivityChildBinding is a generated data binding class of activity_child.xml layout.
 */
abstract class ViewModelBindingActivity<VB : ViewBinding, VM : ViewModel>(
    bindingFactory: (LayoutInflater) -> VB,
    viewModelFactory: (ViewModelBindingActivity<VB, VM>) -> VM
) : BaseViewBindingActivity<VB>(bindingFactory) {

    /**
     * This variable contains the view model instance for this activity.
     */
    protected val viewModel: VM by lazy { viewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setViewModelBindingData(viewModel)
    }

    /**
     * Use this method to assign/set view model data to view binding
     */
    abstract fun VB.setViewModelBindingData(vm: VM)
}
