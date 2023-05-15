package com.edutionAdminLearning.core_ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.edutionAdminLearning.core_ui.viewmodel.BaseViewModel

/**
 * Use this activity class as base class if you have to deal with view model and data binding in activity.
 * While extending this class follow the below mentioned coding pattern

    class PathshalaActivity: ViewModelBindingActivity<ActivityPathshalaBinding, PathshalaViewModel>(
        ActivityPathshalaBinding::inflate,
        { ViewModelProvider(it).get(PathshalaViewModel::class.java) }
    )

 *  in above example ChildViewModel is view model class and ActivityChildBinding is a generated data binding class of activity_child.xml layout.
 */
abstract class PWViewModelBindingActivity<VB : ViewBinding, VM : BaseViewModel>(
    bindingFactory: (LayoutInflater) -> VB,
) : PWViewBindingActivity<VB>(bindingFactory) {

    /**
     * This variable contains the view model instance for this activity.
     */
    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Use this method to assign/set view model data to view binding
     */
}
