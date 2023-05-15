package com.edutionLearning.network.doman

interface UseCaseSuspend<Params, Return> {
    suspend operator fun invoke(params: Params): Return
}