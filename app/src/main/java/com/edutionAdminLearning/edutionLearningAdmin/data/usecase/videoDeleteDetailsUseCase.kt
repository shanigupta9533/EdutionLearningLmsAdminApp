package com.edutionAdminLearning.edutionLearningAdmin.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetVideoDetailsDeleteTimeLies = MyResult<Unit, CompleteApiError<Error>>

class VideoDeleteDetailsUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
) : UseCaseSuspend<String, GetVideoDetailsDeleteTimeLies> {
    override suspend fun invoke(params: String): GetVideoDetailsDeleteTimeLies {
        return lmsEdutionDataSource.videoDetailsDelete(params)
    }
}