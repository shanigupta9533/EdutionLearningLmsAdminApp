package com.edutionAdminLearning.edutionlearningadminapp.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.core.result.map
import com.edutionAdminLearning.edutionlearningadminapp.data.mapper.NotificationMapper
import com.edutionAdminLearning.edutionlearningadminapp.data.model.NotificationData
import com.edutionAdminLearning.edutionlearningadminapp.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetNotificationTimeLies = MyResult<List<NotificationData>, CompleteApiError<Error>>

class GetNotificationUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
    private val notificationMapper: NotificationMapper
) : UseCaseSuspend<Unit, GetNotificationTimeLies> {
    override suspend fun invoke(params: Unit): GetNotificationTimeLies {
        return lmsEdutionDataSource.getNotification().map(notificationMapper::dtoToDomain)
    }
}