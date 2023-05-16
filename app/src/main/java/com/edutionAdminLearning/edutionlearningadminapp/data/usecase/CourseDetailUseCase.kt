package com.edutionAdminLearning.edutionlearningadminapp.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.core.result.map
import com.edutionAdminLearning.edutionlearningadminapp.data.mapper.CoursesDetailsMapper
import com.edutionAdminLearning.edutionlearningadminapp.data.model.CoursesDetailsData
import com.edutionAdminLearning.edutionlearningadminapp.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetCourseDetailsTimeLies = MyResult<List<CoursesDetailsData>?, CompleteApiError<Error>>

class CourseDetailUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
    private val coursesDetailsMapper: CoursesDetailsMapper
) : UseCaseSuspend<Unit, GetCourseDetailsTimeLies> {
    override suspend fun invoke(params: Unit): GetCourseDetailsTimeLies {
        return lmsEdutionDataSource.coursesDetails().map(coursesDetailsMapper::dtoToDomain)
    }
}