package com.edutionAdminLearning.edutionLearningAdmin.activity

import android.app.Dialog
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import com.edutionAdminLearning.core_ui.activity.PWViewBindingActivity
import com.edutionAdminLearning.core_ui.view.dialog.ProgressCustomDialog
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.ActivityMainBinding
import com.edutionAdminLearning.edutionLearningAdmin.fragment.StartLandingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : PWViewBindingActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {

    private var pDialog: Dialog? = null

    private val navHost by lazy {
        supportFragmentManager
            .findFragmentById(R.id.navGraphContainer) as NavHostFragment?
    }

    private val navController by lazy {
        navHost?.navController ?: throw Error("NavController should not be null")
    }

    override fun ActivityMainBinding.setViewBindingVariables() {

        navView.setNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.user_details -> {

                }

                R.id.course_details -> {
                    AppNavigationActivity.start(
                        context = this@MainActivity,
                        fwdLocation = StartLandingFragment.FWD_LOCATION_COURSES)
                }

                R.id.banner_details -> {
                    AppNavigationActivity.start(
                        context = this@MainActivity,
                        fwdLocation = StartLandingFragment.FWD_LOCATION_BANNERS)
                }

                R.id.notification -> {
                    AppNavigationActivity.start(
                        context = this@MainActivity,
                        fwdLocation = StartLandingFragment.FWD_LOCATION_NOTIFICATION)
                }

                R.id.admin_access -> {
                }

                R.id.logout -> {
                    showProgressBar(getString(R.string.please_wait))
                }

            }

            drawerLayout.closeDrawer(GravityCompat.START)

            return@setNavigationItemSelectedListener true
        }

        mainLayout.toolbarAskSaarthi.appCompatImageView.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

    }

    private fun setNavigationGraph() {
        navController.setGraph(navController.navInflater.inflate(R.navigation.nav_graph).apply {
            setStartDestination(R.id.edutionLandingFragment)
        }, intent.extras)
    }

    fun onBackClick() {
        navHost.handBackPressed()
    }

    override fun onBackPressed() {
        navHost.handBackPressed()
    }

    override fun ActivityMainBinding.setViewBindingData() {
        setNavigationGraph()
    }

    private fun showProgressBar(message: String?) {
        if (this.isDestroyed || this.isFinishing) return
        if (pDialog == null) pDialog = ProgressCustomDialog.Companion.progressDialog(this, message)
        try {
            if (pDialog?.isShowing?.not() == true && !this.isFinishing) {
                pDialog?.setCancelable(false)
                pDialog?.show()
            }
        } catch (e: Exception) {
            Log.i("TAG", "showProgressBar: " + e.message)
        }
    }

    private fun hideProgress() {
        try {
            if (pDialog != null && pDialog?.isShowing == true) pDialog?.dismiss()
        } catch (e: java.lang.Exception) {
            Log.i("TAG", "showProgressBar: " + e.message)
        }
    }


}