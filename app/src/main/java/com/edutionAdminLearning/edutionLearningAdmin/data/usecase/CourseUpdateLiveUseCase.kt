package com.edutionAdminLearning.edutionLearningAdmin.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetCoursesUpdateLiveTimeLies = MyResult<Unit, CompleteApiError<Error>>

class CourseUpdateLiveUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
) : UseCaseSuspend<String, GetCoursesUpdateLiveTimeLies> {
    override suspend fun invoke(params: String): GetCoursesUpdateLiveTimeLies {
        return lmsEdutionDataSource.coursesUpdateLive(params)
    }
}