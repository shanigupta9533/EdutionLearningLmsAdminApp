package com.edutionAdminLearning.edutionLearningAdmin.activity

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.edutionAdminLearning.core.network.NetworkCallStatus
import com.edutionAdminLearning.core_ui.activity.PWViewBindingActivity
import com.edutionAdminLearning.core_ui.view.dialog.ProgressCustomDialog
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.ActivityMainBinding
import com.edutionAdminLearning.edutionLearningAdmin.fragment.StartLandingFragment
import com.edutionAdminLearning.edutionLearningAdmin.utils.checkNotificationPermission
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.AdminDetailsViewModel
import com.edutionAdminLearning.network.managers.NetworkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : PWViewBindingActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {

    private var pDialog: Dialog? = null
    val viewModel: AdminDetailsViewModel by viewModels()

    @Inject
    lateinit var networkManager: NetworkManager

    private val navHost by lazy {
        supportFragmentManager
            .findFragmentById(R.id.navGraphContainer) as NavHostFragment?
    }

    private val navController by lazy {
        navHost?.navController ?: throw Error("NavController should not be null")
    }

    override fun ActivityMainBinding.setViewBindingVariables() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission(onGranted = {
            }, onPermissionDenied = {
            })
        }

        lifecycleScope.launch {
            NetworkCallStatus.onServerDown.collect {
                if (it)
                    Toast.makeText(this@MainActivity, "server is down...", Toast.LENGTH_SHORT).show()
            }
        }

        navView.setNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.user_details -> {

                }

                R.id.course_details -> {
                    AppNavigationActivity.start(
                        context = this@MainActivity,
                        fwdLocation = StartLandingFragment.FWD_LOCATION_COURSES
                    )
                }

                R.id.banner_details -> {
                    AppNavigationActivity.start(
                        context = this@MainActivity,
                        fwdLocation = StartLandingFragment.FWD_LOCATION_BANNERS
                    )
                }

                R.id.notification -> {
                    AppNavigationActivity.start(
                        context = this@MainActivity,
                        fwdLocation = StartLandingFragment.FWD_LOCATION_NOTIFICATION
                    )
                }

                R.id.admin_access -> {
                    AppNavigationActivity.start(
                        context = this@MainActivity,
                        fwdLocation = StartLandingFragment.FWD_LOCATION_ADMIN_ACCESS
                    )
                }

                R.id.logout -> {

                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle(R.string.do_you_want_logout)
                    builder.setPositiveButton(R.string.logout) { dialogInterface, i ->
                        showProgressBar(getString(R.string.please_wait))
                        lifecycleScope.launch {
                            viewModel.getUserLogout()?.let {
                                hideProgress()
                                networkManager.loginManager?.logout()
                                Intent(this@MainActivity, LoginActivity::class.java).apply {
                                    startActivity(this)
                                    finishAffinity()
                                }
                            }
                        }
                    }
                    builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
                    val dialog = builder.create()
                    dialog.show()
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