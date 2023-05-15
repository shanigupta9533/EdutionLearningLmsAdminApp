package com.edutionAdminLearning.core_ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.edutionAdminLearning.core_ui.fragment.BaseFragment

/**
 * Use this activity class as base class if you have to deal with data binding in activity.
 * While extending this class follow the below mentioned coding pattern
 *
 *  class ChildActivity: ViewBindingActivity<ActivityChildBinding>(ActivityChildBinding::inflate)
 *
 *  in above example ActivityChildBinding is a generated data binding class of activity_child.xml layout.
 */
abstract class PWViewBindingActivity<VB : ViewBinding>(
    private val layoutId: (LayoutInflater) -> VB
) : BaseActivity() {

    /**
     * This variable contains the binding data binding instance of associate layout file.
     * Use this variable to deal view elements in code
     */
    protected val binding: VB by lazy { layoutId(layoutInflater) }

    open fun VB.setViewBindingVariables(){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root.also {
            binding.setViewBindingVariables()
        })
        binding.setViewBindingData()
    }

    /**
     * Use this method to assign/set data to binding and view
     */
    abstract fun VB.setViewBindingData()

    protected fun NavHostFragment?.handBackPressed() {
        this?.apply {
            val backHandled = getCurrentFragment()?.onBackPressed() ?: false
            if (!backHandled) super.onBackPressed()
        }
    }

    private fun NavHostFragment?.getCurrentFragment(): BaseFragment? {
        return try {
            this?.childFragmentManager?.fragments?.firstOrNull() as? BaseFragment
        } catch (ex: Throwable) {
            null
        }
    }
}
