package com.edutionAdminLearning.edutionLearningAdmin.data.usecase

import com.edutionAdminLearning.core.result.CompleteApiError
import com.edutionAdminLearning.core.result.Error
import com.edutionAdminLearning.core.result.MyResult
import com.edutionAdminLearning.edutionLearningAdmin.data.dto.PurchaseSubmitDto
import com.edutionAdminLearning.edutionLearningAdmin.data.remote.LmsEdutionDataSource
import com.edutionAdminLearning.network.doman.UseCaseSuspend
import javax.inject.Inject

typealias GetPurchaseDetailsSubmitTimeLies = MyResult<Unit, CompleteApiError<Error>>

class PurchaseDetailsSubmitUseCase @Inject constructor(
    private val lmsEdutionDataSource: LmsEdutionDataSource,
) : UseCaseSuspend<PurchaseSubmitDto, GetPurchaseDetailsSubmitTimeLies> {
    override suspend fun invoke(params: PurchaseSubmitDto): GetPurchaseDetailsSubmitTimeLies {
        return lmsEdutionDataSource.getPurchaseSubmit(params)
    }
}