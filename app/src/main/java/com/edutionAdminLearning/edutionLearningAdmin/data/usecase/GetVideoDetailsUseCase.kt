package com.edutionAdminLearning.edutionLearningAdmin.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.core.result.map
import com.edutionAdminLearning.edutionLearningAdmin.data.mapper.VideoDetailsMapper
import com.edutionAdminLearning.edutionLearningAdmin.data.model.VideoDetails
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetVideoDetailsTimeLies = MyResult<VideoDetails?, CompleteApiError<Error>>

class GetVideoDetailsUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
    private val videoDetailsMapper: VideoDetailsMapper
): UseCaseSuspend<String, GetVideoDetailsTimeLies> {
    override suspend fun invoke(params: String): GetVideoDetailsTimeLies {
        return lmsEdutionDataSource.getVideoDetails(params).map(videoDetailsMapper::dtoToDomain)
    }
}