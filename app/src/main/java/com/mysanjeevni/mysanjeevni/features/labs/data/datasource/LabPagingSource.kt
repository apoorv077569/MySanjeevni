package com.mysanjeevni.mysanjeevni.features.labs.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.labs.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTest

class LabPagingSource(
    private val api: ApiService
) : PagingSource<Int, LabTest>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, LabTest> {

        val page = params.key ?: 1

        Log.d("PAGING_TEST", "Loading Page = $page")

        return try {

            val response = api.getLabTests(
                page = page,
                limit = 20
            )

            Log.d(
                "PAGING_TEST",
                "Response Code = ${response.code()}"
            )

            Log.d(
                "PAGING_TEST",
                "Response Body = ${response.body()}"
            )

            val body = response.body()

            // DTO -> Domain
            val tests = body?.tests?.map {
                Log.d("PAGING_TEST", "Test Name = ${it.name}")
                it.toDomain()
            } ?: emptyList()

            Log.d(
                "PAGING_TEST",
                "Fetched Tests = ${tests.size}"
            )

            LoadResult.Page(
                data = tests,
                prevKey = null,
                nextKey = page + 1
            )

        } catch (e: Exception) {

            Log.e(
                "PAGING_TEST",
                "Paging Error",
                e
            )

            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, LabTest>
    ): Int? = state.anchorPosition
}