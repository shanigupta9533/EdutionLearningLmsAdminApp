package com.edutionAdminLearning.core.result

import android.accounts.NetworkErrorException
import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core.utils.fromJsonToObject
import kotlinx.coroutines.CancellationException
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownServiceException

/**
 * This class is used to provide a result for an operation, which may be succesfull or fail.
 */

sealed class MyResult<out V, out E> {
    data class Ok<out V>(val value: V) : MyResult<V, Nothing>()
    data class Err<out E>(val error: E) : MyResult<Nothing, E>()
}

fun <T> Response<T>.getError(): CompleteApiError<Any?> = try {
    when {
        code() in 200 .. 300 -> NoError
        // code().isUnauthorized() -> UnauthorizedError
        code() in 400..599 -> this.getExpectedError()
        !isSuccessful -> ServerApiError
        else -> UnknownApiError
    }
} catch (e: Throwable) {
    e.printStackTrace()
    e.getApiError()
}

private fun <T> Response<T>.getExpectedError(): CompleteApiError<Any?> {
    val errorResponse = errorBody()?.string()
    val apiResponse: ApiResponse<*>?= fromJsonToObject(errorResponse)
    return apiResponse?.error?.let { ExpectedApiError(it) } ?: ServerApiError
}

fun <T> Response<T>.getGenError(): GenericApiError? = try {
    when {
        code() == 200 -> null
        code() in 402..599 -> GenericApiError.Server
        !isSuccessful -> GenericApiError.Server
        else -> GenericApiError.Unknown
    }
} catch (e: Throwable) {
    e.printStackTrace()
    ApiErrorHandling.handleGeneric(e).error
}

fun Throwable.getApiError() = when (this) {
    is NoConnectivityException -> NoInternetApiError
    is NetworkErrorException -> NetworkApiError
    is CancellationException -> CancelError
    is ClassCastException, is UnknownServiceException, is ConnectException -> ServerApiError
    else -> UnknownApiError
}


suspend fun <V> exeApi(perform: suspend () -> Response<V>): MyResult<V, CompleteApiError<Any?>> =
    try {
        with(perform()) {
            val error = getError()
            if (error is NoError) {
                body()?.let { MyResult.Ok(it) } ?: MyResult.Err(ServerApiError)
            } else MyResult.Err(error)
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        MyResult.Err(e.getApiError())
    }

suspend fun <V> exeApiData(perform: suspend () -> Response<V>): MyResult<V, String?> =
    try {
        with(perform()) {
            val error = getError()
            if (error is NoError) {
                body()?.let { MyResult.Ok(it) } ?: MyResult.Err(this.errorBody()?.string())
            } else MyResult.Err(this.errorBody()?.string())
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        MyResult.Err(e.message ?: String.EMPTY)
    }

suspend fun <V> exeApiGen(perform: suspend () -> Response<V>): MyResult<V, GenericApiError> =
    try {
        with(perform()) {
            val error = getGenError()
            if (error == null) {
                body()?.let { MyResult.Ok(it) } ?: MyResult.Err(GenericApiError.Server)
            } else MyResult.Err(error)
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        MyResult.Err(ApiErrorHandling.handleGeneric(e).error)
    }


fun <V> exeApiBlocking(perform: () -> Response<V>): MyResult<V, CompleteApiError<Any?>> = try {
    with(perform()) {
        val error = getError()
        if (error is NoError) {
            body()?.let { MyResult.Ok(it) } ?: MyResult.Err(ServerApiError)
        } else MyResult.Err(error)
    }
} catch (e: Throwable) {
    e.printStackTrace()
    MyResult.Err(e.getApiError())
}

/**
 * Get response data if response success is true, also handle response code if success is not true, return error code as [ExpectedError]
 */
fun <V, E> MyResult<ApiResponse<V>, CompleteApiError<E>>.getDataResult(
    handleExpectedErrorCode: ((error: Error) -> CompleteApiError<E>)? = null
): MyResult<V, CompleteApiError<E>> = when (this) {
    is MyResult.Ok ->
        if (value.success)
            MyResult.Ok(value.data)
        else MyResult.Err(
            value.error?.let {
                handleExpectedErrorCode?.invoke(
                    it
                ) ?: ExpectedApiError(value.error)
            } ?: ExpectedApiError(value.error)
        )
    is MyResult.Err -> this
}

fun <V> MyResult<ApiResponse<V>, GenericApiError>.getDataResultGen(): MyResult<V, GenericApiError> =
    when (this) {
        is MyResult.Ok ->
            if (value.success)
                MyResult.Ok(value.data)
            else MyResult.Err(GenericApiError.Server)
        is MyResult.Err -> this
    }

fun <V> MyResult<ApiResponse<V>, String?>.getDataGen(): MyResult<V, String?> =
    when (this) {
        is MyResult.Ok ->
            if (value.success)
                MyResult.Ok(value.data)
            else MyResult.Err(value.error?.message)
        is MyResult.Err -> this
    }

/**
 * Calls the specified function [block] and returns its encapsulated result if
 * invocation was successful, catching and encapsulating any thrown exception
 * as a failure.
 */
inline fun <V> myRunCatching(block: () -> V): MyResult<V, Throwable> {
    return try {
        MyResult.Ok(block())
    } catch (e: Throwable) {
        MyResult.Err(e)
    }
}

/**
 * Calls the specified function [block] with [this] value as its receiver and
 * returns its encapsulated result if invocation was successful, catching and
 * encapsulating any thrown exception as a failure.
 */
inline infix fun <T, V> T.myRunCatching(block: T.() -> V): MyResult<V, Throwable> {
    return try {
        MyResult.Ok(block())
    } catch (e: Throwable) {
        MyResult.Err(e)
    }
}

/**
 * Maps this [MyResult<V, E>][MyResult] to [MyResult<U, E>][MyResult] by either applying the
 * [transform] function if this [MyResult] is [Ok], or returning this [Err].
 */
inline infix fun <V, E, U> MyResult<V, E>.andThen(
    transform: (V) -> MyResult<U, E>
): MyResult<U, E> {
    return when (this) {
        is MyResult.Ok -> transform(value)
        is MyResult.Err -> this
    }
}

/**
 * Maps this [MyResult<V, E>][MyResult] to [MyResult<U, E>][MyResult] by either applying the
 * [transform] function to the [value][Ok.value] if this [MyResult] is [Ok], or returning this
 * [Err].
 */
inline infix fun <V, E, U> MyResult<V, E>.map(transform: (V) -> U): MyResult<U, E> {
    return when (this) {
        is MyResult.Ok -> MyResult.Ok(transform(value))
        is MyResult.Err -> this
    }
}

fun <V, E> MyResult<V, E>.value(): V? {
    return when (this) {
        is MyResult.Ok -> value
        is MyResult.Err -> null
    }
}

/**
 * Maps this [MyResult<V, E>][MyResult] to [MyResult<V, F>][MyResult] by either applying the
 * [transform] function to the [error][Err.error] if this [MyResult] is [Err], or returning this
 * [Ok].
 */
inline infix fun <V, E, F> MyResult<V, E>.mapError(transform: (E) -> F): MyResult<V, F> {
    return when (this) {
        is MyResult.Ok -> this
        is MyResult.Err -> MyResult.Err(transform(error))
    }
}

/**
 * Maps this [MyResult<V, E>][MyResult] to [U] by applying either the [success] function if this
 * [MyResult] is [Ok], or the [failure] function if this [MyResult] is an [Err]. Both of these
 * functions must return the same type ([U]).
 */
inline fun <V, E, U> MyResult<V, E>.mapBoth(success: (V) -> U, failure: (E) -> U): U {
    return when (this) {
        is MyResult.Ok -> success(value)
        is MyResult.Err -> failure(error)
    }
}

/**
 * Invokes an [action] if this [MyResult] is [Ok].
 */
inline infix fun <V, E> MyResult<V, E>.onSuccess(action: (V) -> Unit): MyResult<V, E> {
    if (this is MyResult.Ok) {
        action(value)
    }

    return this
}

/**
 * Invokes an [action] if this [MyResult] is [Err].
 */
inline infix fun <V, E> MyResult<V, E>.onFailure(action: (E) -> Unit): MyResult<V, E> {
    if (this is MyResult.Err) {
        action(error)
    }

    return this
}

/**
 * Returns [result] if this [MyResult] is [Err], otherwise this [Ok].
 */
public infix fun <V, E> MyResult<V, E>.or(result: MyResult<V, E>): MyResult<V, E> {
    return when (this) {
        is MyResult.Ok -> this
        is MyResult.Err -> result
    }
}

/**
 * Returns the [transformation][transform] of the [error][Err.error] if this [MyResult] is [Err],
 * otherwise this [Ok].
 */
public inline infix fun <V, E> MyResult<V, E>.orElse(
    transform: (E) -> MyResult<V, E>
): MyResult<V, E> {
    return when (this) {
        is MyResult.Ok -> this
        is MyResult.Err -> transform(error)
    }
}
