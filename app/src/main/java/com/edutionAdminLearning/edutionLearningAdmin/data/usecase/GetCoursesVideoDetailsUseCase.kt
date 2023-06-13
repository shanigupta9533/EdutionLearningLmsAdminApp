package com.edutionAdminLearning.edutionLearningAdmin.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.core.result.map
import com.edutionAdminLearning.edutionLearningAdmin.data.mapper.CoursesVideoDetailsMapper
import com.edutionAdminLearning.edutionLearningAdmin.data.model.CoursesVideo
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetCoursesVideoDetailsTimeLies = MyResult<List<CoursesVideo>, CompleteApiError<Error>>

class GetCoursesVideoDetailsUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
    private val videoDetailsMapper: CoursesVideoDetailsMapper
): UseCaseSuspend<String, GetCoursesVideoDetailsTimeLies> {
    override suspend fun invoke(params: String): GetCoursesVideoDetailsTimeLies {
        return lmsEdutionDataSource.getVideoDetails(params).map(videoDetailsMapper::dtoToDomain)
    }
}