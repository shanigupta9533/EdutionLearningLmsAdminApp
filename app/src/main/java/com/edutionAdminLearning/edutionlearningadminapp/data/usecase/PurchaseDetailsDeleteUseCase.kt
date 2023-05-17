package com.edutionAdminLearning.edutionlearningadminapp.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionlearningadminapp.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetPurchaseDetailsDeleteTimeLies = MyResult<Unit, CompleteApiError<Error>>

class PurchaseDetailsDeleteUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
) : UseCaseSuspend<String, GetPurchaseDetailsDeleteTimeLies> {
    override suspend fun invoke(params: String): GetPurchaseDetailsDeleteTimeLies {
        return lmsEdutionDataSource.purchaseDelete(params)
    }
}