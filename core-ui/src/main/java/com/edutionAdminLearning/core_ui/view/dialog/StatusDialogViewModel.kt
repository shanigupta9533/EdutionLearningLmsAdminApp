package com.edutionAdminLearning.core_ui.view.dialog

import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core_ui.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class StatusDialogViewModel : BaseViewModel() {

    private val _status = MutableStateFlow(StatusDialog.Status.SUCCESS)
    val status = _status.asStateFlow()

    val noInternet =
        status.map { it == StatusDialog.Status.INTERNET_CONNECTION_ERROR }.stateInEagerly(false)
    val success = status.map { it == StatusDialog.Status.SUCCESS }.stateInEagerly(false)
    val failed = status.map { it == StatusDialog.Status.FAILED }.stateInEagerly(false)
    val info = status.map { it == StatusDialog.Status.INFO }.stateInEagerly(false)

    private val _title = MutableStateFlow(String.EMPTY)
    val title = _title.asStateFlow()

    private val _message = MutableStateFlow(String.EMPTY)
    val message = _message.asStateFlow()

    fun setData(status: StatusDialog.Status, title: String, message: String) {
        this._status.value = status
        _title.value = title
        _message.value = message
    }

}