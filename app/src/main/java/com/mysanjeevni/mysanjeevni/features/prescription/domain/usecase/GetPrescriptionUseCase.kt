package com.mysanjeevni.mysanjeevni.features.prescription.domain.usecase



import com.mysanjeevni.mysanjeevni.features.prescription.domain.model.Prescription
import com.mysanjeevni.mysanjeevni.features.prescription.domain.repository.PrescriptionRepository
import javax.inject.Inject

class GetPrescriptionsUseCase @Inject constructor(
    private val repository: PrescriptionRepository,

) {

    suspend operator fun invoke(
        userId: String,
        consultationId: String? = null

    ): Result<List<Prescription>> {

        return repository.getPrescriptions(
            userId = userId,
            consultationId = consultationId
        )
    }
}