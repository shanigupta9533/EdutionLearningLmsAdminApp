package com.edutionLearning.core_ui.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.edutionLearning.core_ui.utils.LastClickTimeSingleton

/**
 * Use this fragment class as base class if you have to deal with data binding in fragment.
 * While extending this class follow the below mentioned coding pattern
 *
 *  class ChildFragment: ViewBindingFragment<FragmentChildBinding>(FragmentChildBinding::inflate)
 *
 *  in above example FragmentChildBinding is a generated data binding class of fragment_child.xml layout.
 */
abstract class ViewBindingFragment<VB : ViewBinding>(
    private val bindingFactory: (LayoutInflater) -> VB
) : BaseFragment() {

    /**
     * This variable contains the binding data binding instance of associate layout file.
     * Use this variable to deal view elements in code
     */

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    val viewLifecycleScope: LifecycleCoroutineScope?
        get() {
            return if (isAdded) viewLifecycleOwner.lifecycleScope
            else null
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = bindingFactory(layoutInflater)
            return binding.root.also {
                binding.setViewBindingVariables()
            }
        }
        return binding.root
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

    open fun isActivityBackPress(): Boolean {
        return false
    }
    open fun  debounceCallBack(action: () -> Unit,  time: Long = 1000L) {
        if (SystemClock.elapsedRealtime() - LastClickTimeSingleton.lastClickTime < time) return
        else action()
        LastClickTimeSingleton.lastClickTime = SystemClock.elapsedRealtime()
    }
}
