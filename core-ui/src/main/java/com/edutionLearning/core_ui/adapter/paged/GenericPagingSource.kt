package com.edutionLearning.core_ui.adapter.paged

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.edutionLearning.core.result.GenericApiError
import com.edutionLearning.core.result.MyResult
import com.edutionLearning.core.result.UiApiError
import com.edutionLearning.core.result.toBasicUi

class GenericPagingSource<T : Any>(
    private val fetchDataFactory: suspend (page: Int) -> MyResult<List<T>, GenericApiError>,
    private val defaultPage: Int = DEFAULT_PAGE,
    private val loadOnlyFirstPage: Boolean = false,
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
//        return state.anchorPosition
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val currentPageNumber = params.key ?: defaultPage
            when (val response: MyResult<List<T>, GenericApiError> =
                fetchDataFactory(currentPageNumber)) {
                is MyResult.Ok -> LoadResult.Page(
                    data = response.value,
                    prevKey = null, // Only paging forward.
                    nextKey = if (response.value.isEmpty() || (loadOnlyFirstPage && currentPageNumber == 1)) null else currentPageNumber + 1
                )
                is MyResult.Err -> LoadResult.Error(
                    when (response.error.toBasicUi()) {
                        UiApiError.NoInternet -> PageError.NoInternet
                        UiApiError.Generic -> PageError.Generic
                    }
                )
            }

        } catch (e: Exception) {
            LoadResult.Error(
                e
            )
        }
    }

    companion object {
        private const val DEFAULT_PAGE = 1
    }
}

sealed class PageError : Throwable() {
    object NoInternet : PageError()
    object Generic : PageError()
}