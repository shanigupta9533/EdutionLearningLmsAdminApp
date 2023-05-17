package com.edutionAdminLearning.edutionlearningadminapp.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionlearningadminapp.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetNotificationDeleteTimeLies = MyResult<Unit, CompleteApiError<Error>>

class NotificationDeleteUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
) : UseCaseSuspend<String, GetNotificationDeleteTimeLies> {
    override suspend fun invoke(params: String): GetNotificationDeleteTimeLies {
        return lmsEdutionDataSource.notificationDelete(params)
    }
}