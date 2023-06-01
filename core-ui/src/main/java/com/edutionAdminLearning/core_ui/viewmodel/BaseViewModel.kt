package com.edutionAdminLearning.core_ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edutionAdminLearning.core.result.CompleteUiApiError
import com.edutionAdminLearning.core.result.UiApiError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.edutionAdminLearning.core.result.Error

abstract class BaseViewModel : ViewModel() {

    // Handle loading state
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    // Handle generic Error
    private val _uiApiError = MutableSharedFlow<UiApiError?>(extraBufferCapacity = 1)
    val uiApiError = _uiApiError.asSharedFlow()

    fun UiApiError.show() {
        _uiApiError.tryEmit(this)
    }

    fun showGenericError(error: UiApiError) {
        _uiApiError.tryEmit(error)
    }

    // Handle complete Error
    private val _completeUiApiError =
        MutableSharedFlow<CompleteUiApiError<Error>?>(extraBufferCapacity = 1)
    val completeUiApiError = _completeUiApiError.asSharedFlow()

    fun CompleteUiApiError<Error>.show() {
        _completeUiApiError.tryEmit(this)
    }

    private val _completeUiApiErrorToast =
        MutableSharedFlow<CompleteUiApiError<Error>?>(extraBufferCapacity = 1)
    val completeUiApiErrorToast = _completeUiApiErrorToast.asSharedFlow()

    fun CompleteUiApiError<Error>.showErrorToast() {
        _completeUiApiErrorToast.tryEmit(this)
    }

    private val _toastMessage = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val toastMessage = _toastMessage.asSharedFlow()

    fun Int.toast() {
        _toastMessage.tryEmit(this)
    }

    // to show success message
    private val _successMessage = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val successMessage = _successMessage.asSharedFlow()

    fun showSuccess(stringRes: Int) {
        _successMessage.tryEmit(stringRes)
    }

    // Start loading
    fun startLoading() {
        _loading.value = true
    }

    // Stop loading
    fun stopLoading() {
        _loading.value = false
    }

    fun combineLoading(vararg l: Flow<Boolean>) = viewModelScope.launch {
        combine(l.asList()) { loadings ->
            // Any loading is true
            loadings.any { it }
        }.collect {
            if (it) startLoading() else stopLoading()
        }
    }

    fun <T> Flow<T>.stateInEagerly(defaultValue: T) =
        stateIn(viewModelScope, SharingStarted.Eagerly, defaultValue)
}