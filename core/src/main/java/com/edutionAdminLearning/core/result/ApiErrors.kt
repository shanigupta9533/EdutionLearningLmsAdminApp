package com.edutionAdminLearning.core.result

import android.accounts.NetworkErrorException
import android.util.Log


enum class GenericApiError { NoInternet, Network, Server, Unknown }

enum class UiApiError { NoInternet, Generic }

fun GenericApiError.toBasicUi() = when (this) {
    GenericApiError.NoInternet -> UiApiError.NoInternet
    GenericApiError.Network, GenericApiError.Server, GenericApiError.Unknown -> UiApiError.Generic
}

sealed class CompleteApiError<in T>
object NoError : CompleteApiError<Any?>()
object NoInternetApiError : CompleteApiError<Any?>()
object NetworkApiError : CompleteApiError<Any?>()
object CancelError : CompleteApiError<Any?>()
object ServerApiError : CompleteApiError<Any?>()
object UnknownApiError : CompleteApiError<Any?>()
class ExpectedApiError<T>(val which: T) : CompleteApiError<T>()

sealed class CompleteUiApiError<in T>
object NoInternetUiApiError : CompleteUiApiError<Any?>()
object GenericUiApiError : CompleteUiApiError<Any?>()
object CancelUiApiError : CompleteUiApiError<Any?>()
class ExpectedUiApiError<T>(val which: T) : CompleteUiApiError<T>()

fun <T> CompleteApiError<T>.toBasicUi(): CompleteUiApiError<T> = when (this) {
    NoInternetApiError -> NoInternetUiApiError
    NetworkApiError, ServerApiError, UnknownApiError -> GenericUiApiError
    is ExpectedApiError -> ExpectedUiApiError(this.which)
    is CancelError -> CancelUiApiError
    else -> GenericUiApiError
}

fun CompleteApiError<Error>.getMessage(): String? {
    return if (this is ExpectedApiError) which.message
    else null
}

fun CompleteApiError<Error>.getStatusCode(): Int? {
    return if (this is ExpectedApiError) which.status
    else null
}

fun CompleteApiError<Error>.getWarningType(): String? {
    return if (this is ExpectedApiError) which.warningType
    else null
}

object ApiErrorHandling {

    fun handleGeneric(e: Throwable) = when (e) {
        is NoConnectivityException -> MyResult.Err(GenericApiError.NoInternet)
        is NetworkErrorException -> MyResult.Err(GenericApiError.Network)
        else -> {
            e.message?.let { Log.e("ApiError123456", it) }
            MyResult.Err(GenericApiError.Unknown)
        }
    }

    fun handle(e: Throwable) = when (e) {
        is NoConnectivityException -> MyResult.Err(NoInternetApiError)
        is NetworkErrorException -> MyResult.Err(NetworkApiError)
        is ClassCastException -> {
            e.message?.let { Log.e("ApiError123456", it) }
            MyResult.Err(ServerApiError)
        }
        else -> {
            e.message?.let { Log.e("ApiError123456", it) }
            MyResult.Err(UnknownApiError)
        }
    }
}
