package com.edutionAdminLearning.edutionLearningAdmin.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias retryUseCaseVideoTimeLies = MyResult<Boolean, CompleteApiError<Error>>

class RetryVideosUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
) : UseCaseSuspend<String, retryUseCaseVideoTimeLies> {
    override suspend fun invoke(params: String): retryUseCaseVideoTimeLies {
        return lmsEdutionDataSource.retryVideoUseCase(params)
    }
}
