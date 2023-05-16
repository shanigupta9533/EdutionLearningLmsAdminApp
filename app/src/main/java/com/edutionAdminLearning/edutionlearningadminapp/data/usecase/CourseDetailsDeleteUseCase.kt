package com.edutionAdminLearning.edutionlearningadminapp.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionlearningadminapp.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetCourseDetailsDeleteTimeLies = MyResult<Unit, CompleteApiError<Error>>

class CourseDetailDeleteUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
) : UseCaseSuspend<String, GetCourseDetailsDeleteTimeLies> {
    override suspend fun invoke(params: String): GetCourseDetailsDeleteTimeLies {
        return lmsEdutionDataSource.coursesDetailsDelete(params)
    }
}