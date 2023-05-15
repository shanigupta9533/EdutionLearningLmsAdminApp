package com.edutionAdminLearning.core_ui.view.dialog

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.edutionAdminLearning.core.result.*
import com.edutionAdminLearning.core.type.value
import com.edutionAdminLearning.core_ui.R
import com.edutionAdminLearning.core_ui.extensions.toastL
import com.edutionAdminLearning.core_ui.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

fun Fragment.handleUiApiError(viewModel: BaseViewModel) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewModel.uiApiError.filterNotNull().collect {
            when (it) {
                UiApiError.Generic -> showFailed()
                UiApiError.NoInternet -> showNoInternet()
            }
        }
    }
}

fun Fragment.handleCompleteUiApiError(viewModel: BaseViewModel) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewModel.completeUiApiError.filterNotNull().collect {
            when (it) {
                is GenericUiApiError -> showFailed()
                is NoInternetUiApiError -> showNoInternet()
                is ExpectedUiApiError -> {
                    showFailed(message = it.which.message.value)
                }
                is CancelUiApiError -> {
                    // Do nothing
                }
            }
        }
    }

    viewLifecycleOwner.lifecycleScope.launch {
        viewModel.completeUiApiErrorToast.filterNotNull().collect {
            when (it) {
                is GenericUiApiError -> toastL(getString(R.string.error_response))
                is NoInternetUiApiError -> toastL(getString(R.string.no_internet_connection))
                is ExpectedUiApiError -> {
                    toastL(it.which.message)
                }
                is CancelUiApiError -> {
                    // Do nothing
                }
            }
        }
    }

    viewLifecycleOwner.lifecycleScope.launch {
        viewModel.toastMessage.filterNotNull().collect {
            toastL(getString(it))
        }
    }
}

fun Fragment.handleSuccessDialog(viewModel: BaseViewModel) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewModel.successMessage.filter { it > 0 }.collect {
            showSuccess(message = getString(it))
        }
    }
}