package com.edutionAdminLearning.edutionlearningadminapp.viewmodel

import androidx.lifecycle.viewModelScope
import com.edutionAdminLearning.core.result.getMessage
import com.edutionAdminLearning.core.result.onFailure
import com.edutionAdminLearning.core.result.onSuccess
import com.edutionAdminLearning.core_ui.viewmodel.BaseViewModel
import com.edutionAdminLearning.edutionlearningadminapp.data.model.CoursesDetailsData
import com.edutionAdminLearning.edutionlearningadminapp.data.model.PurchaseDetails
import com.edutionAdminLearning.edutionlearningadminapp.data.usecase.CourseDetailDeleteUseCase
import com.edutionAdminLearning.edutionlearningadminapp.data.usecase.CourseDetailUseCase
import com.edutionAdminLearning.edutionlearningadminapp.data.usecase.PurchaseDetailUseCase
import com.edutionAdminLearning.edutionlearningadminapp.data.usecase.PurchaseDetailsSubmitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailsViewModel @Inject constructor(
    private val courseDetailUseCase: CourseDetailUseCase,
    private val courseDetailDeleteUseCase: CourseDetailDeleteUseCase,
    private val purchaseDetailUseCase: PurchaseDetailUseCase,
    private val purchaseDetailsSubmitUseCase: PurchaseDetailsSubmitUseCase
) : BaseViewModel() {

    private val _getAllCourses = MutableStateFlow<List<CoursesDetailsData>?>(null)
    val getAllCourses = _getAllCourses.asStateFlow()

    private val _getAllPurchaseDetails = MutableSharedFlow<List<PurchaseDetails>?>(extraBufferCapacity = 1)
    val getAllPurchaseDetails = _getAllPurchaseDetails.asSharedFlow()

    private val _respondSuccess = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val respondSuccess = _respondSuccess.asSharedFlow()

    private val _errorDetails = MutableSharedFlow<String?>(extraBufferCapacity = 1)
    val errorDetails = _errorDetails.asSharedFlow()

    fun getAllCourses() {
        startLoading()
        viewModelScope.launch {
            courseDetailUseCase(Unit)
                .onSuccess {
                    stopLoading()
                    it?.let { _getAllCourses.value = it }
                }
                .onFailure { result ->
                    stopLoading()
                    _errorDetails.tryEmit(result.getMessage())
                }
        }
    }

    fun deleteCourseDetails() {
        startLoading()
        viewModelScope.launch {
            courseDetailUseCase(Unit)
                .onSuccess {
                    stopLoading()
                    _respondSuccess.tryEmit(true)
                }
                .onFailure { result ->
                    stopLoading()
                    _respondSuccess.tryEmit(false)
                    _errorDetails.tryEmit(result.getMessage())
                }
        }
    }

    fun getAllPurchaseDetails() {
        startLoading()
        viewModelScope.launch {
            purchaseDetailUseCase(Unit)
                .onSuccess {
                    stopLoading()
                    _getAllPurchaseDetails.tryEmit(it)
                }
                .onFailure { result ->
                    stopLoading()
                    _errorDetails.tryEmit(result.getMessage())
                }
        }
    }

    fun submitPurchaseDetails() {
        startLoading()
        viewModelScope.launch {
            purchaseDetailsSubmitUseCase(Unit)
                .onSuccess {
                    stopLoading()
                    _respondSuccess.tryEmit(true)
                }
                .onFailure { result ->
                    stopLoading()
                    _respondSuccess.tryEmit(false)
                    _errorDetails.tryEmit(result.getMessage())
                }
        }
    }

}