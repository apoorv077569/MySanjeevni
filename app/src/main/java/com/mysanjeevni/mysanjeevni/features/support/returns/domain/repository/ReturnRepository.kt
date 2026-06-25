package com.mysanjeevni.mysanjeevni.features.support.returns.domain.repository

import com.mysanjeevni.mysanjeevni.features.support.returns.data.dto.ReturnRequestDto
import com.mysanjeevni.mysanjeevni.features.support.returns.domain.model.ReturnRequest

interface ReturnRepository {

    suspend fun submitReturn(
        request: ReturnRequestDto
    ): Result<String>

    suspend fun getReturns(
        userId: String
    ): Result<List<ReturnRequest>>
}