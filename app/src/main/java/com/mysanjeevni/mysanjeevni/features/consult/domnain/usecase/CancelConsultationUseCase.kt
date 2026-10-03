package com.mysanjeevni.mysanjeevni.features.consult.domnain.usecase


import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.CancelConsultationResponse
import com.mysanjeevni.mysanjeevni.features.consult.domnain.repository.ConsultRepository
import javax.inject.Inject

class CancelConsultationUseCase @Inject constructor(
    private val repository: ConsultRepository
) {
    suspend operator fun invoke(
        consultationId: String
    ): Result<CancelConsultationResponse> {
        return repository.cancelConsultation(
            consultationId
        )
    }
}