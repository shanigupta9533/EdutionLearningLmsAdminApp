package com.edutionAdminLearning.edutionLearningAdmin.activity

import android.content.Context
import android.content.Intent
import androidx.navigation.fragment.NavHostFragment
import com.edutionAdminLearning.core_ui.activity.PWViewBindingActivity
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.ActivityAppNavigationBinding
import com.edutionAdminLearning.edutionLearningAdmin.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppNavigationActivity : PWViewBindingActivity<ActivityAppNavigationBinding>(
    ActivityAppNavigationBinding::inflate
) {

    private val navHost by lazy {
        supportFragmentManager
            .findFragmentById(R.id.navGraphContainer) as NavHostFragment?
    }

    private val navController by lazy {
        navHost?.navController ?: throw Error("NavController should not be null")
    }

    override fun ActivityAppNavigationBinding.setViewBindingData() {
        setNavigationGraph()
    }

    private fun setNavigationGraph() {
        navController.setGraph(navController.navInflater.inflate(R.navigation.home_nav_graph).apply {
            setStartDestination(R.id.startLandingFragment)
        }, intent.extras)
    }

    companion object {

        private const val FWD_LOCATION = "fwdLocation"

        @JvmStatic
        fun start(context: Context, fwdLocation: String? = null) = with(context) {
            startActivity(Intent(this, AppNavigationActivity::class.java).apply {
                putExtra(FWD_LOCATION, fwdLocation)
            })
        }

    }

    override fun onBackPressed() {
        navHost.handBackPressed()
    }

}