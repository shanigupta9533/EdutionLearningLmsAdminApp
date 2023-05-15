package com.edutionAdminLearning.core.utils

import kotlinx.coroutines.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

class CoroutineHelper private constructor(private val scope: CoroutineScope = GlobalScope) {

    // A function that launches a coroutine in an IO thread and return a JOB
    fun <T> launchOnIO(block: suspend CoroutineScope.() -> T): Job {
        return scope.launch(Dispatchers.IO) {
            block()
        }
    }

    fun launchOnIO(block: () -> Unit)  {
         scope.launch(Dispatchers.IO) {
            block()
        }
    }

    // A function that launches a coroutine in a Default thread and return a JOB
    fun <T> launchOnDefault(block: suspend CoroutineScope.() -> T): Job {
        return scope.launch(Dispatchers.Default) {
            block()
        }
    }

    fun launchOnMain(block: () -> Unit)  {
        scope.launch(Dispatchers.Main) {
            block()
        }
    }

    // A function that launches a coroutine in a Main thread and return a JOB
    fun <T> launchOnMain(block: suspend CoroutineScope.() -> T): Job {
        return scope.launch(Dispatchers.Main) {
            block()
        }
    }


    fun launchOnDefault(block: () -> Unit)  {
        scope.launch(Dispatchers.Default) {
            block()
        }
    }
    // A function that switches to the main thread and returns a result
    suspend fun <T> withMain(block: suspend () -> T): T {
        return withContext(Dispatchers.Main) {
            block()
        }
    }

    // A function that switches to the IO thread and returns a result
    suspend fun <T> withIO(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            block()
        }
    }

    // A function that switches to the Default thread and returns a result
    suspend fun <T> withDefault(block: suspend () -> T): T {
        return withContext(Dispatchers.Main) {
            block()
        }
    }

    // A function that handles exceptions and cancellation
    suspend fun tryCatchFinally(
        tryBlock: suspend () -> Unit,
        catchBlock: suspend (Throwable) -> Unit,
        finallyBlock: suspend () -> Unit
    ) {
        try {
            tryBlock()
        } catch (e: Throwable) {
            catchBlock(e)
        } finally {
            finallyBlock()
        }
    }

    // A function that converts a callback-based function to a suspending function
    suspend fun <T> suspendCallback(block: (Continuation<T>) -> Unit): T {
        return suspendCoroutine { continuation ->
            block(continuation)
        }
    }

    // A function that cancels the scope
    fun cancel() {
        scope.cancel()
    }

    // A companion object that provides a static method for Java interoperability
    companion object {

        // A static method that creates an instance of CoroutineHelper with a given scope
        @JvmOverloads
        @JvmStatic
        fun create(scope: CoroutineScope = GlobalScope): CoroutineHelper {
            return CoroutineHelper(scope)
        }
    }
}

