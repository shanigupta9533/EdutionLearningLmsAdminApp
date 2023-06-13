package com.edutionAdminLearning.edutionLearningAdmin.viewmodel

import androidx.lifecycle.viewModelScope
import com.edutionAdminLearning.core.result.onFailure
import com.edutionAdminLearning.core.result.onSuccess
import com.edutionAdminLearning.core.result.toBasicUi
import com.edutionAdminLearning.core_ui.viewmodel.BaseViewModel
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.data.model.CoursesDetailsData
import com.edutionAdminLearning.edutionLearningAdmin.data.model.CoursesVideo
import com.edutionAdminLearning.edutionLearningAdmin.data.model.PurchaseDetails
import com.edutionAdminLearning.edutionLearningAdmin.data.model.VideoData
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.CourseDetailDeleteUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.CourseDetailUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.CourseUpdateLiveUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.CourseVideosDeleteUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.GetCoursesVideoDetailsUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.PurchaseDetailUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.PurchaseDetailsDeleteUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.PurchaseDetailsSubmitUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.PurchaseDetailsUpdate
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.PurchaseDetailsUpdateUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.RetryVideosUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.VideoDeleteDetailsUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.VideoDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val coursesVideoDetailsUseCase: GetCoursesVideoDetailsUseCase,
    private val courseVideoDeleteUseCase: CourseVideosDeleteUseCase,
    private val courseUpdateLiveUseCase: CourseUpdateLiveUseCase,
    private val videoDetailsUseCase: VideoDetailUseCase,
    private val videoDeleteDetailsUseCase: VideoDeleteDetailsUseCase,
    private val retryVideosUseCase: RetryVideosUseCase
) : BaseViewModel() {

    private val _getAllCourses = MutableSharedFlow<List<CoursesDetailsData>?>(extraBufferCapacity = 1)
    val getAllCourses = _getAllCourses.asSharedFlow()

    private val _coursesVideoPlayer = MutableSharedFlow<List<CoursesVideo>>(extraBufferCapacity = 1)
    val coursesVideoPlayer = _coursesVideoPlayer.asSharedFlow()

    private val _videoDetails = MutableSharedFlow<List<VideoData?>>(extraBufferCapacity = 1)
    val videoDetails = _videoDetails.asSharedFlow()

    private val _getAllPurchaseDetails = MutableSharedFlow<List<PurchaseDetails>?>(extraBufferCapacity = 1)
    val getAllPurchaseDetails = _getAllPurchaseDetails.asSharedFlow()

    private val _respondSuccess = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val respondSuccess = _respondSuccess.asSharedFlow()

    private val _errorDetails = MutableSharedFlow<String?>(extraBufferCapacity = 1)
    val errorDetails = _errorDetails.asSharedFlow()

    private val _deletePurchaseSpec = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val deletePurchaseSpec = _deletePurchaseSpec.asSharedFlow()

    fun deletePurchaseSpec(position: Int){
        _deletePurchaseSpec.tryEmit(position)
    }

    fun getAllCourses() {
        startLoading()
        viewModelScope.launch {
            courseDetailUseCase(Unit)
                .onSuccess {
                    stopLoading()
                    it?.let { _getAllCourses.tryEmit(it) }
                }
                .onFailure {
                    stopLoading()
                    it.toBasicUi().show()
                }
        }
    }

    fun deleteCourseDetails(courseId: String) {
        startLoading()
        viewModelScope.launch {
            courseDetailDeleteUseCase(courseId)
                .onSuccess {
                    stopLoading()
                    _respondSuccess.tryEmit(true)
                }
                .onFailure {
                    stopLoading()
                    it.toBasicUi().show()
                }
        }
    }

    fun getAllPurchaseDetails(courseId: String) {
        startLoading()
        viewModelScope.launch {
            purchaseDetailUseCase(courseId)
                .onSuccess {
                    stopLoading()
                    _getAllPurchaseDetails.tryEmit(it)
                }
                .onFailure {
                    stopLoading()
                    it.toBasicUi().show()
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
                .onFailure {
                    stopLoading()
                    it.toBasicUi().show()
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
                .onFailure {
                    stopLoading()
                    it.toBasicUi().show()
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
                .onFailure {
                    stopLoading()
                    it.toBasicUi().show()
                }
        }
    }

    fun videoDetailsDelete(videoId: String) {
        startLoading()
        viewModelScope.launch {
            videoDeleteDetailsUseCase(videoId)
                .onSuccess {
                    stopLoading()
                    _respondSuccess.tryEmit(true)
                }
                .onFailure {
                    stopLoading()
                    it.toBasicUi().show()
                }
        }
    }

    fun retryVideos(videoId: String) {
        startLoading()
        viewModelScope.launch {
            retryVideosUseCase(videoId)
                .onSuccess {
                    stopLoading()
                    _respondSuccess.tryEmit(true)
                }
                .onFailure {
                    stopLoading()
                    it.toBasicUi().show()
                }
        }
    }

    fun getCoursesVideoDetails(course_id: String) {
        startLoading()
        viewModelScope.launch {
            coursesVideoDetailsUseCase(course_id).onSuccess {
                stopLoading()
                _coursesVideoPlayer.tryEmit(it)
            }.onFailure {
                it.toBasicUi().show()
                stopLoading()
            }
        }
    }

    fun getVideoDetails(keywords: String) {
        startLoading()
        viewModelScope.launch {
            videoDetailsUseCase(keywords).onSuccess {
                stopLoading()
                _videoDetails.tryEmit(it)
            }.onFailure {
                it.toBasicUi().show()
                stopLoading()
            }
        }
    }

    fun coursesVideoDetailsDelete(videoId: String) {
        startLoading()
        viewModelScope.launch {
            courseVideoDeleteUseCase(videoId).onSuccess {
                stopLoading()
                _respondSuccess.tryEmit(true)
            }.onFailure {
                it.toBasicUi().show()
                stopLoading()
            }
        }
    }

    fun updateCourseUpdateLive(courseId: String) {
        startLoading()
        viewModelScope.launch {
            courseUpdateLiveUseCase(courseId).onSuccess {
                stopLoading()
                _respondSuccess.tryEmit(true)
            }.onFailure {
                it.toBasicUi().show()
                stopLoading()
            }
        }
    }

}