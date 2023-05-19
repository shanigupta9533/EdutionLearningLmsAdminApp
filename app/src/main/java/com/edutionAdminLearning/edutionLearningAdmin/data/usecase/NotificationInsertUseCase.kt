package com.edutionAdminLearning.edutionLearningAdmin.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetNotificationSubmitTimeLies = MyResult<Unit, CompleteApiError<Error>>

class NotificationInsertUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
) : UseCaseSuspend<String, GetNotificationSubmitTimeLies> {
    override suspend fun invoke(params: String): GetNotificationSubmitTimeLies {
        return lmsEdutionDataSource.notificationInsert(params)
    }
}