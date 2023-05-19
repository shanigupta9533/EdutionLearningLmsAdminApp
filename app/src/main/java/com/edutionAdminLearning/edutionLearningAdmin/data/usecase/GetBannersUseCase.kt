package com.edutionAdminLearning.edutionLearningAdmin.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.core.result.map
import com.edutionAdminLearning.edutionLearningAdmin.data.mapper.BannerMapper
import com.edutionAdminLearning.edutionLearningAdmin.data.model.BannerData
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetBannerTimeLies = MyResult<List<BannerData>?, CompleteApiError<Error>>

class GetBannersUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
    private val bannerMapper: BannerMapper
) : UseCaseSuspend<String, GetBannerTimeLies> {
    override suspend fun invoke(params: String): GetBannerTimeLies {
        return lmsEdutionDataSource.getAllBanners(keywords = params).map(bannerMapper::dtoToDomain)
    }
}