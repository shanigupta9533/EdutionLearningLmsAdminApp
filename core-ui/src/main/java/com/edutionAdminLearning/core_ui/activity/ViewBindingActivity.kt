package com.edutionAdminLearning.core_ui.activity

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
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
abstract class ViewBindingActivity<VB : ViewBinding>(
    private val layoutId: (LayoutInflater) -> VB
) : AppCompatActivity() {

    /**
     * This variable contains the binding data binding instance of associate layout file.
     * Use this variable to deal view elements in code
     */
    protected val binding: VB by lazy { layoutId(layoutInflater) }

    private var pDialog: Dialog? = null

    private var showInApp = true

    open fun VB.setViewBindingVariables() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setViewBindingVariables()
        binding.setViewToolbar()
        binding.setViewBindingData()
    }

    /**
     * Use this method to set view toolbar
     */
    abstract fun VB.setViewToolbar()

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

    open fun hideProgress() {
        try {
            if (pDialog != null && pDialog?.isShowing == true) pDialog?.dismiss()
        } catch (e: Exception) {
            Log.i("TAG", "hideProgress: "+e.message)
        }
    }

    override fun onStart() {
        super.onStart()
        if (showInApp) {
            showInApp = false
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}
