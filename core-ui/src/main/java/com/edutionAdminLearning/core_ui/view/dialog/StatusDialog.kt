package com.edutionAdminLearning.core_ui.view.dialog

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.edutionAdminLearning.core_ui.R
import com.edutionAdminLearning.core_ui.databinding.DialogStatusBinding
import com.edutionAdminLearning.core_ui.fragment.ViewModelBindingDialogFragment

class StatusDialog(
    private val status: Status,
    private val title: String,
    private val message: String,
) : ViewModelBindingDialogFragment<DialogStatusBinding, StatusDialogViewModel>(
    DialogStatusBinding::inflate
) {

    enum class Status {
        FAILED, INTERNET_CONNECTION_ERROR, INFO, SUCCESS
    }

    override val viewModel: StatusDialogViewModel by viewModels()

    override fun DialogStatusBinding.setViewBindingVariables() {
        vm = viewModel
        controller = this@StatusDialog
        viewModel.setData(status, this@StatusDialog.title, this@StatusDialog.message)
    }

    override fun DialogStatusBinding.setViewBindingData() {

    }

    override fun DialogStatusBinding.setViewModelBindingData() {

    }

    fun onOkClick() {
        dismiss()
    }
}


private fun Fragment.openStatusDialog(status: StatusDialog.Status, title: String, message: String) {
    val statusDialog = StatusDialog(status, title, message)
    statusDialog.isCancelable = false
    statusDialog.show(childFragmentManager, StatusDialog::class.java.name)
}

fun Fragment.showFailed(
    title: String = getString(R.string.status_dialog_title_failed),
    message: String = getString(R.string.status_dialog_message_failed)
) {
    openStatusDialog(StatusDialog.Status.FAILED, title, message)
}

fun Fragment.showNoInternet(
    title: String = getString(R.string.status_dialog_title_no_internet),
    message: String = getString(R.string.status_dialog_message_no_internet)
) {
    openStatusDialog(StatusDialog.Status.INTERNET_CONNECTION_ERROR, title, message)
}

fun Fragment.showInfo(
    title: String,
    message: String
) {
    openStatusDialog(StatusDialog.Status.INFO, title, message)
}

fun Fragment.showSuccess(
    title: String = getString(R.string.status_dialog_title_success),
    message: String = getString(R.string.status_dialog_message_success)
) {
    openStatusDialog(StatusDialog.Status.SUCCESS, title, message)
}