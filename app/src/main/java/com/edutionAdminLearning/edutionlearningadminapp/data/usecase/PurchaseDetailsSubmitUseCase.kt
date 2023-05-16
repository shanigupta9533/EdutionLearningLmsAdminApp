package com.edutionAdminLearning.edutionlearningadminapp.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionlearningadminapp.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetPurchaseDetailsSubmitTimeLies = MyResult<Unit, CompleteApiError<Error>>

class PurchaseDetailsSubmitUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
) : UseCaseSuspend<Unit, GetPurchaseDetailsSubmitTimeLies> {
    override suspend fun invoke(params: Unit): GetPurchaseDetailsSubmitTimeLies {
        TODO("Not yet implemented")
    }
}