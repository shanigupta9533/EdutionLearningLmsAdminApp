package com.edutionLearning.core.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object NetworkCallStatus {

    private val _onServerDown = MutableStateFlow(false)
    val onServerDown = _onServerDown.asStateFlow()

    private val _onCallStatusCode = MutableStateFlow(200 to "")
    val onCallStatusCode = _onCallStatusCode.asStateFlow()

    internal fun postServerDown(isDown: Boolean) {
        _onServerDown.tryEmit(isDown)
    }

    internal fun postCallStatusCode(code: Pair<Int, String>) {
        _onCallStatusCode.tryEmit(code)
    }

}