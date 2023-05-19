package com.edutionAdminLearning.edutionLearningAdmin.viewmodel

import androidx.lifecycle.viewModelScope
import com.edutionAdminLearning.core.result.getMessage
import com.edutionAdminLearning.core.result.onFailure
import com.edutionAdminLearning.core.result.onSuccess
import com.edutionAdminLearning.core_ui.viewmodel.BaseViewModel
import com.edutionAdminLearning.edutionLearningAdmin.data.model.BannerData
import com.edutionAdminLearning.edutionLearningAdmin.data.model.NotificationData
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.BannerDeleteUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.GetBannersUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.GetNotificationUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.NotificationDeleteUseCase
import com.edutionAdminLearning.edutionLearningAdmin.data.usecase.NotificationInsertUseCase
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDetailsViewModel @Inject constructor(
    private val bannersUseCase: GetBannersUseCase,
    private val bannerDeleteUseCase: BannerDeleteUseCase,
    private val getNotificationUseCase: GetNotificationUseCase,
    private val notificationInsertUseCase: NotificationInsertUseCase,
    private val notificationDeleteUseCase: NotificationDeleteUseCase
) : BaseViewModel() {

    private val _coursesBanners = MutableSharedFlow<List<BannerData>?>(extraBufferCapacity = 1)
    val coursesBanners = _coursesBanners.asSharedFlow()

    private val _respondSuccess = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val respondSuccess = _respondSuccess.asSharedFlow()

    private val _userNotification = MutableSharedFlow<List<NotificationData>?>(extraBufferCapacity = 1)
    val userNotification = _userNotification.asSharedFlow()

    private val _errorDetails = MutableSharedFlow<String?>(extraBufferCapacity = 1)
    val errorDetails = _errorDetails.asSharedFlow()

    fun getBannersData(keywords: String) {
        startLoading()
        viewModelScope.launch {
            bannersUseCase(keywords)
                .onSuccess {
                    stopLoading()
                    _coursesBanners.tryEmit(it)
                }
                .onFailure {
                    stopLoading()
                }
        }
    }

    fun bannerDelete(bannerId: String) {
        startLoading()
        viewModelScope.launch {
            bannerDeleteUseCase(bannerId)
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

    fun getNotification() {
        startLoading()
        viewModelScope.launch {
            getNotificationUseCase(Unit).onSuccess {
                _userNotification.tryEmit(it)
                stopLoading()
            }.onFailure {
                stopLoading()
            }
        }
    }

    fun notificationInsert(notification: String) {
        startLoading()
        viewModelScope.launch {
            notificationInsertUseCase(notification).onSuccess {
                _respondSuccess.tryEmit(true)
                stopLoading()
            }.onFailure {
                _respondSuccess.tryEmit(false)
                stopLoading()
            }
        }
    }

    fun notificationDelete(notificationId: String) {
        startLoading()
        viewModelScope.launch {
            notificationDeleteUseCase(notificationId).onSuccess {
                _respondSuccess.tryEmit(true)
                stopLoading()
            }.onFailure {
                _respondSuccess.tryEmit(false)
                stopLoading()
            }
        }
    }

}