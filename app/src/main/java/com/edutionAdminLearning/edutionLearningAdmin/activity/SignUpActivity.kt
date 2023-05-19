package com.edutionAdminLearning.edutionLearningAdmin.activity

import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core_ui.activity.PWViewModelBindingActivity
import com.edutionAdminLearning.edutionLearningAdmin.databinding.ActivitySignUpBinding
import com.edutionAdminLearning.edutionLearningAdmin.viewmodel.AdminDetailsViewModel
import com.edutionAdminLearning.network.managers.NetworkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.edutionAdminLearning.core_ui.R
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.SignUpSubmitDto

@AndroidEntryPoint
class SignupActivity : PWViewModelBindingActivity<ActivitySignUpBinding, AdminDetailsViewModel>(
    ActivitySignUpBinding::inflate
) {

    @Inject
    lateinit var networkManager: NetworkManager

    override val viewModel: AdminDetailsViewModel by viewModels()

    override fun ActivitySignUpBinding.setViewBindingData() {

        lifecycleScope.launch {
            viewModel.loading.collect {
                parentOfProgressInclude.progressBar.visibility = if (it) View.VISIBLE else View.GONE
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

                    Intent(this@SignupActivity, SignupActivity::class.java).apply{
                        startActivity(this)
                        finish()
                    }

                }
            }
        }

        lifecycleScope.launch {
            viewModel.errorDetails.collect {
                if (it.isNullOrEmpty().not()) {
                    showErrorByServer(it)
                    viewModel.errorValidate(String.EMPTY)
                }
            }
        }

        signupButton.setOnClickListener {

            userName.error = null
            phone.error = null
            password.error = null

            when {
                TextUtils.isEmpty(userName.text.toString().trim()) -> {
                    userName.error = getString(R.string.name_is_required)
                    userName.requestFocus()
                }

                TextUtils.isEmpty(phone.text.toString().trim()) -> {
                    phone.error = getString(R.string.phone_is_required)
                    phone.requestFocus()
                }

                phone.text.toString().trim().length < 10 -> {
                    phone.error = getString(R.string.phone_number_is_not_valid)
                    phone.requestFocus()
                }

                TextUtils.isEmpty(password.text.toString().trim()) || password.text.toString().trim().length < 4 -> {
                    password.error = getString(R.string.password_must_be_four_digit)
                    password.requestFocus()
                }

                else -> {

                    viewModel.signUpUser(
                        SignUpSubmitDto(
                            phone = phone.text.toString().trim(),
                            name = userName.text.toString().trim(),
                            password = password.text.toString().trim()
                        )
                    )

                }
            }

        }

    }

    private fun ActivitySignUpBinding.showErrorByServer(validate: String?) {

        if (validate?.contains("name") == true) {
            userName.error = validate
            userName.requestFocus()
        } else if (validate?.contains("phone") == true) {
            phone.error = validate
            phone.requestFocus()
        } else if (validate?.contains("password") == true) {
            password.error = validate
            password.requestFocus()
        } else {
            userName.error = validate
            userName.requestFocus()
        }

    }
}