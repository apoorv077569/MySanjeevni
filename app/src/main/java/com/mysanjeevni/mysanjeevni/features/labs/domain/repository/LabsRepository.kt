package com.mysanjeevni.mysanjeevni.features.labs.domain.repository

import androidx.paging.PagingData
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.CreateLabBookingRequestDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.LabTestsPage
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabBooking
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTest
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTestDetail
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.Serviceability
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.SlotsRequestParams
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.SlotsResult
import kotlinx.coroutines.flow.Flow

interface LabsRepository {
    fun getLabTestPaging(): Flow<PagingData<LabTest>>
    suspend fun getLabTestDetail(id: String): Result<LabTestDetail>

    suspend fun bookLabTest(
        request: CreateLabBookingRequestDto
    ): Result<LabBooking>

    suspend fun checkServiceability(testId: String, pincode: String): Result<Serviceability>

    suspend fun searchSlots(params: SlotsRequestParams): Result<SlotsResult>

}
