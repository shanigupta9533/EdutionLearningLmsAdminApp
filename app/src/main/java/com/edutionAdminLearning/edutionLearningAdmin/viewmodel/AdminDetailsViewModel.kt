package com.edutionAdminLearning.edutionLearningAdmin.viewmodel

import androidx.lifecycle.viewModelScope
import com.edutionAdminLearning.core.result.getMessage
import com.edutionAdminLearning.core.result.onFailure
import com.edutionAdminLearning.core.result.onSuccess
import com.edutionAdminLearning.core.result.toBasicUi
import com.edutionAdminLearning.core.result.value
import com.edutionAdminLearning.core_ui.viewmodel.BaseViewModel
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.LoginSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.SignUpSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.data.model.UserDetailsData
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.AddAdminUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.GetLogoutUserUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.GetSplashUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDetailsViewModel @Inject constructor(
    private val userDetailsUseCase: AddAdminUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val getSplashUseCase: GetSplashUseCase,
    private val logoutUserUseCase: GetLogoutUserUseCase
) : BaseViewModel() {

    private val _getUserDetails = MutableStateFlow<UserDetailsData?>(null)
    val getUserDetails = _getUserDetails.asStateFlow()

    private val _errorDetails = MutableSharedFlow<String?>(extraBufferCapacity = 1)
    val errorDetails = _errorDetails.asSharedFlow()

    private val _userLogout = MutableSharedFlow<UserDetailsData?>(extraBufferCapacity = 1)
    val userLogout = _userLogout.asSharedFlow()

    fun errorValidate(error: String) {
        _errorDetails.tryEmit(error)
    }

    fun signUpUser(signUpSubmitDto: SignUpSubmitDto) {
        startLoading()
        viewModelScope.launch {
            userDetailsUseCase(signUpSubmitDto)
                .onSuccess {
                    stopLoading()
                    it?.let { _getUserDetails.value = it } ?: _errorDetails.tryEmit("User Not Found...")
                }
                .onFailure { result ->
                    stopLoading()
                    _errorDetails.tryEmit(result.getMessage())
                }
        }
    }

    fun loginUser(loginSubmitDto: LoginSubmitDto) {
        startLoading()
        viewModelScope.launch {
            loginUserUseCase(loginSubmitDto)
                .onSuccess {
                    stopLoading()
                    it?.let { _getUserDetails.value = it } ?: _errorDetails.tryEmit("User Not Found...")
                }
                .onFailure {
                    stopLoading()
                    _errorDetails.tryEmit(it.getMessage())
                }
        }
    }

    suspend fun getUserLogout(): UserDetailsData? {
        startLoading()
        return logoutUserUseCase(Unit)
            .onSuccess {
                stopLoading()
                _userLogout.tryEmit(it)
            }
            .onFailure {
                stopLoading()
                it.toBasicUi()
            }.value()
    }

    fun getSplash() {
        startLoading()
        viewModelScope.launch {
            getSplashUseCase(Unit)
                .onSuccess {
                    stopLoading()
                    it?.let { _getUserDetails.value = it } ?: _errorDetails.tryEmit("User Not Found...")
                }
                .onFailure {
                    stopLoading()
                    _errorDetails.tryEmit(it.getMessage())
                }
        }
    }

}