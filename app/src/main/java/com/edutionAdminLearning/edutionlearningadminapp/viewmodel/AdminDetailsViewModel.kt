package com.edutionAdminLearning.edutionlearningadminapp.viewmodel

import androidx.lifecycle.viewModelScope
import com.edutionAdminLearning.core.result.getMessage
import com.edutionAdminLearning.core.result.onFailure
import com.edutionAdminLearning.core.result.onSuccess
import com.edutionAdminLearning.core_ui.viewmodel.BaseViewModel
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.LoginSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.SignUpSubmitDto
import com.edutionAdminLearning.edutionlearningadminapp.data.model.UserDetailsData
import com.edutionAdminLearning.edutionlearningadminapp.data.usecase.AddAdminUseCase
import com.edutionAdminLearning.edutionlearningadminapp.data.usecase.LoginUserUseCase
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
    private val loginUserUseCase: LoginUserUseCase
) : BaseViewModel() {

    private val _getUserDetails = MutableStateFlow<UserDetailsData?>(null)
    val getUserDetails = _getUserDetails.asStateFlow()

    private val _errorDetails = MutableSharedFlow<String?>(extraBufferCapacity = 1)
    val errorDetails = _errorDetails.asSharedFlow()

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

    fun loginUser(loginSubmitDto: LoginSubmitDto){
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

}