package com.edutionAdminLearning.edutionLearningAdmin.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetCoursesDeleteTimeLies = MyResult<Unit, CompleteApiError<Error>>

class CoursesDeleteUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
) : UseCaseSuspend<String, GetCoursesDeleteTimeLies> {
    override suspend fun invoke(params: String): GetCoursesDeleteTimeLies {
        return lmsEdutionDataSource.videoDeleteDetails(params)
    }
}