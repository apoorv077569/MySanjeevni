package com.mysanjeevni.mysanjeevni.features.labs.domain.usecase

import com.mysanjeevni.mysanjeevni.features.labs.data.dto.CreateLabBookingRequestDto
import com.mysanjeevni.mysanjeevni.features.labs.domain.repository.LabsRepository
import javax.inject.Inject

class BookLabTestUseCase @Inject constructor(
    private val repository: LabsRepository
) {

    suspend operator fun invoke(
        request: CreateLabBookingRequestDto
    ) = repository.bookLabTest(request)
}