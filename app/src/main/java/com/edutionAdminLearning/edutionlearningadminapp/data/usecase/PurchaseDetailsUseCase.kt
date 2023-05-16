package com.edutionAdminLearning.edutionlearningadminapp.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.core.result.map
import com.edutionAdminLearning.edutionlearningadminapp.data.mapper.PurchaseDetailsMapper
import com.edutionAdminLearning.edutionlearningadminapp.data.model.PurchaseDetails
import com.edutionAdminLearning.edutionlearningadminapp.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetPurchaseDetailsTimeLies = MyResult<List<PurchaseDetails>?, CompleteApiError<Error>>

class PurchaseDetailUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
    private val purchaseDetailsMapper: PurchaseDetailsMapper
) : UseCaseSuspend<Unit, GetPurchaseDetailsTimeLies> {
    override suspend fun invoke(params: Unit): GetPurchaseDetailsTimeLies {
        return lmsEdutionDataSource.getPurchaseDetails().map(purchaseDetailsMapper::dtoToDomain)
    }
}