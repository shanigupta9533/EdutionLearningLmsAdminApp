package com.edutionAdminLearning.edutionLearningAdmin.activity

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.edutionAdminLearning.core.network.NetworkUtils
import com.edutionAdminLearning.core_ui.activity.PWViewModelBindingActivity
import com.edutionAdminLearning.edutionLearningAdmin.R
import com.edutionAdminLearning.edutionLearningAdmin.databinding.ActivitySplashBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.AdminDetailsViewModel
import com.edutionAdminLearning.network.managers.NetworkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : PWViewModelBindingActivity<ActivitySplashBinding, AdminDetailsViewModel>(
    ActivitySplashBinding::inflate
) {

    @Inject
    lateinit var networkManager: NetworkManager

    override val viewModel: AdminDetailsViewModel by viewModels()

    override fun ActivitySplashBinding.setViewBindingData() {

        lifecycleScope.launch {
            viewModel.errorDetails.collect{
                noInternetConnection.visibility = View.VISIBLE
                noConnectionText.text = getString(R.string.something_went_wrong)
            }
        }

        refreshButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            noInternetConnection.visibility = View.GONE
            binding.splashCallApiWithToken(networkManager.loginManager?.getToken())
        }

        lifecycleScope.launch {
            viewModel.loading.collect{
                progressBar.visibility = if(it){
                    noInternetConnection.visibility = View.GONE
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getUserDetails.collect{
                it?.let {
                    if(it.phone.isNullOrEmpty().not()) {
                        Intent(this@SplashActivity, MainActivity::class.java).apply {
                            startActivity(this)
                            finishAffinity()
                        }
                    } else {
                        Intent(this@SplashActivity, LoginActivity::class.java).apply {
                            startActivity(this)
                            finishAffinity()
                        }
                    }
                }
            }
        }

        splashCallApiWithToken(networkManager.loginManager?.getToken())

    }

    private fun ActivitySplashBinding.splashCallApiWithToken(token: String?) {
        if(token.isNullOrEmpty()){
            Intent(this@SplashActivity, LoginActivity::class.java).apply {
                startActivity(this)
                finishAffinity()
            }
        } else {

            if(NetworkUtils.isConnectedToInternet(this@SplashActivity)){
                viewModel.getSplash()
            } else {
                progressBar.visibility = View.GONE
                noInternetConnection.visibility = View.VISIBLE
            }
        }
    }

}