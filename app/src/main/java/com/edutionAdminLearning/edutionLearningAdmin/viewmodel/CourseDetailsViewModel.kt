package com.edutionAdminLearning.edutionLearningAdmin.viewmodel

import androidx.lifecycle.viewModelScope
import com.edutionAdminLearning.core.result.getMessage
import com.edutionAdminLearning.core.result.onFailure
import com.edutionAdminLearning.core.result.onSuccess
import com.edutionAdminLearning.core_ui.viewmodel.BaseViewModel
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.data.model.CoursesDetailsData
import com.edutionAdminLearning.edutionLearningAdmin.data.model.CoursesVideo
import com.edutionAdminLearning.edutionLearningAdmin.data.model.PurchaseDetails
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.CourseDetailDeleteUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.CourseDetailUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.GetVideoDetailsUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.PurchaseDetailUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.PurchaseDetailsDeleteUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.PurchaseDetailsSubmitUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.PurchaseDetailsUpdate
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.PurchaseDetailsUpdateUseCase
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
    private val purchaseDetailsSubmitUseCase: PurchaseDetailsSubmitUseCase,
    private val purchaseDetailsUpdateUseCase: PurchaseDetailsUpdateUseCase,
    private val purchaseDetailsDeleteUseCase: PurchaseDetailsDeleteUseCase,
    private val videoDetailsUseCase: GetVideoDetailsUseCase,
    private val courseVideoDeleteUseCase: CourseDetailDeleteUseCase
) : BaseViewModel() {

    private val _getAllCourses = MutableStateFlow<List<CoursesDetailsData>?>(null)
    val getAllCourses = _getAllCourses.asStateFlow()

    private val _coursesVideoPlayer = MutableSharedFlow<List<CoursesVideo>>(extraBufferCapacity = 1)
    val coursesVideoPlayer = _coursesVideoPlayer.asSharedFlow()

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

    fun courseDetailsDelete(courseId: String) {
        startLoading()
        viewModelScope.launch {
            courseDetailDeleteUseCase(courseId)
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

    fun submitPurchaseDetails(purchaseSubmitDto: PurchaseSubmitDto) {
        startLoading()
        viewModelScope.launch {
            purchaseDetailsSubmitUseCase(purchaseSubmitDto)
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

    fun updatePurchaseDetails(purchaseDetailsUpdate: PurchaseDetailsUpdate) {
        startLoading()
        viewModelScope.launch {
            purchaseDetailsUpdateUseCase(purchaseDetailsUpdate)
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

    fun purchaseDetailsDelete(purchaseId: String) {
        startLoading()
        viewModelScope.launch {
            purchaseDetailsDeleteUseCase(purchaseId)
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

    fun getVideoDetails(course_id: String) {
        startLoading()
        viewModelScope.launch {
            videoDetailsUseCase(course_id).onSuccess {
                stopLoading()
                _coursesVideoPlayer.tryEmit(it?.courseVideo ?: emptyList())
            }.onFailure {
                stopLoading()
            }
        }
    }

    fun videoDetailsDelete(videoId: String) {
        startLoading()
        viewModelScope.launch {
            courseVideoDeleteUseCase(videoId).onSuccess {
                stopLoading()
                _respondSuccess.tryEmit(true)
            }.onFailure {
                _respondSuccess.tryEmit(false)
                stopLoading()
            }
        }
    }


}