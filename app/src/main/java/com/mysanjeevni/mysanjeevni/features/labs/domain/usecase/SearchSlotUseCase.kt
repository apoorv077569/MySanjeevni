package com.mysanjeevni.mysanjeevni.features.labs.domain.usecase

import com.mysanjeevni.mysanjeevni.features.labs.domain.model.SlotsRequestParams
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.SlotsResult
import com.mysanjeevni.mysanjeevni.features.labs.domain.repository.LabsRepository
import javax.inject.Inject

class SearchSlotsUseCase @Inject constructor(
    private val repository: LabsRepository
) {
    suspend operator fun invoke(params: SlotsRequestParams): Result<SlotsResult> {
        if (params.appointmentDate.isBlank() || params.pincode.isBlank()) {
            return Result.failure(IllegalArgumentException("Appointment date and pincode are required"))
        }
        return repository.searchSlots(params)
    }
}