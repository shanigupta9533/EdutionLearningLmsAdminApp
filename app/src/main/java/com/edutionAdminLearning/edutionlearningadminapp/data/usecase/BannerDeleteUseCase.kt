package com.edutionAdminLearning.edutionlearningadminapp.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionlearningadminapp.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetBannerDeleteTimeLies = MyResult<Unit, CompleteApiError<Error>>

class BannerDeleteUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
) : UseCaseSuspend<String, GetBannerDeleteTimeLies> {
    override suspend fun invoke(params: String): GetBannerDeleteTimeLies {
        return lmsEdutionDataSource.deleteBanner(params)
    }
}