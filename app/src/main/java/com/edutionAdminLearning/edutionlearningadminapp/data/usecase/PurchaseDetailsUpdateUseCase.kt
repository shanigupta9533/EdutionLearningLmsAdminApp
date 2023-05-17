package com.edutionAdminLearning.edutionlearningadminapp.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionlearningadminapp.data.dto.PurchaseDetailsUpdateDto
import com.edutionAdminLearning.edutionlearningadminapp.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetPurchaseDetailsUpdateTimeLies = MyResult<Unit, CompleteApiError<Error>>

data class PurchaseDetailsUpdate(
    val purchaseDetailsUpdateDto: PurchaseDetailsUpdateDto,
    val purchaseId: String
)

class PurchaseDetailsUpdateUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
) : UseCaseSuspend<PurchaseDetailsUpdate, GetPurchaseDetailsUpdateTimeLies> {
    override suspend fun invoke(params: PurchaseDetailsUpdate): GetPurchaseDetailsUpdateTimeLies {
        return lmsEdutionDataSource.getPurchaseUpdate(
            purchaseId = params.purchaseId,
            purchaseDetailsUpdateDto = params.purchaseDetailsUpdateDto
        )
    }
}