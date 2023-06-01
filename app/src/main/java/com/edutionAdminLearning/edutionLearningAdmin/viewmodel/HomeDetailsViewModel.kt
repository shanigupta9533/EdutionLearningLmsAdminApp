package com.edutionAdminLearning.edutionLearningAdmin.viewmodel

import androidx.lifecycle.viewModelScope
import com.edutionAdminLearning.core.result.getMessage
import com.edutionAdminLearning.core.result.onFailure
import com.edutionAdminLearning.core.result.onSuccess
import com.edutionAdminLearning.core.result.toBasicUi
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
                    it.toBasicUi().show()
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
                    result.toBasicUi().show()
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
                it.toBasicUi().show()
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
                it.toBasicUi().show()
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
                stopLoading()
                it.toBasicUi().show()
            }
        }
    }

}