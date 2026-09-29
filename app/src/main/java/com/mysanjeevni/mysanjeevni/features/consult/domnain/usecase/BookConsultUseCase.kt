package com.mysanjeevni.mysanjeevni.features.consult.domnain.usecase

import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.BookConsultationRequest
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.BookConsultationResponse
import com.mysanjeevni.mysanjeevni.features.consult.domnain.repository.ConsultRepository
import javax.inject.Inject

class BookConsultationUseCase @Inject constructor(
    private val repository: ConsultRepository
) {

    suspend operator fun invoke(
        request: BookConsultationRequest
    ): Result<BookConsultationResponse> {
        return repository.bookConsultation(request)
    }
}