package com.edutionAdminLearning.core_ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding

/**
 * Use this fragment class as base class if you have to deal with data binding in fragment.
 * While extending this class follow the below mentioned coding pattern
 *
 *  class ChildFragment: ViewBindingFragment<FragmentChildBinding>(FragmentChildBinding::inflate)
 *
 *  in above example FragmentChildBinding is a generated data binding class of fragment_child.xml layout.
 */
abstract class ViewBindingDialogFragment<VB: ViewBinding>(
    private val bindingFactory: (LayoutInflater) -> VB
): BaseDialogFragment() {

    /**
     * This variable contains the binding data binding instance of associate layout file.
     * Use this variable to deal view elements in code
     */
    protected val binding: VB by lazy { bindingFactory(layoutInflater) }

    val viewLifecycleScope: LifecycleCoroutineScope
        get() {
            return viewLifecycleOwner.lifecycleScope
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root.also {
            binding.setViewBindingVariables()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setViewBindingData()
    }

    /**
     * Use this method to assign/set value to binding variablesa
     */
    abstract fun VB.setViewBindingVariables()

    /**
     * Use this method to assign/set data to views
     */
    abstract fun VB.setViewBindingData()
}
