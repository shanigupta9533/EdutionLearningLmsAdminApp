package com.edutionAdminLearning.edutionLearningAdmin.activity

import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core_ui.activity.PWViewModelBindingActivity
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.LoginSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.databinding.ActivityLoginBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.AdminDetailsViewModel
import com.edutionAdminLearning.network.managers.NetworkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.edutionAdminLearning.core_ui.R
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : PWViewModelBindingActivity<ActivityLoginBinding, AdminDetailsViewModel>(
    ActivityLoginBinding::inflate
) {

    override val viewModel: AdminDetailsViewModel by viewModels()

    override fun ActivityLoginBinding.setViewBindingData() {
    }

    @Inject
    lateinit var networkManager: NetworkManager

    override fun ActivityLoginBinding.setViewBindingVariables() {

        lifecycleScope.launch {
            viewModel.loading.collect {
                progressBar.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.getUserDetails.collect {
                it?.let { userDetails ->

                    networkManager.loginManager?.apply {
                        setToken(userDetails.token)
                        setName(userDetails.name)
                        setPhone(userDetails.phone)
                        setUserId(userDetails.userId)
                    }

                    Intent(this@LoginActivity, MainActivity::class.java).apply {
                        startActivity(this)
                        finishAffinity()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.errorDetails.collect {
                if (it.isNullOrEmpty().not()) {
                    showErrorByServer(validate = it)
                    viewModel.errorValidate(String.EMPTY)
                }
            }
        }

        loginButton.setOnClickListener {

            phone.error = null
            passwordToggle.error = null

            when {
                TextUtils.isEmpty(phone.text.toString().trim()) -> {
                    phone.error = getString(R.string.phone_is_required_for_login)
                    phone.requestFocus()
                }

                phone.text.toString().trim().length < 10 -> {
                    phone.error = getString(R.string.phone_number_is_not_valid)
                    phone.requestFocus()
                }

                TextUtils.isEmpty(passwordToggle.text.toString().trim()) -> {
                    passwordToggle.error = getString(R.string.password_is_required_for_login)
                    passwordToggle.requestFocus()
                }

                else -> {
                    viewModel.loginUser(
                        LoginSubmitDto(
                            phone = phone.text.toString().trim(),
                            password = passwordToggle.text.toString().trim()
                        )
                    )
                }
            }

        }

    }

    private fun ActivityLoginBinding.showErrorByServer(validate: String?) {

        if (validate?.contains("phone") == true) {
            phone.error = validate
            phone.requestFocus()
        } else if (validate?.contains("password") == true) {
            passwordToggle.error = validate
            passwordToggle.requestFocus()
        } else {
            phone.error = validate
            phone.requestFocus()
        }

    }

}