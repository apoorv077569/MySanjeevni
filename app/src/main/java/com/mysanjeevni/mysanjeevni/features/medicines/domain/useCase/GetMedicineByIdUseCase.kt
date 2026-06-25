package com.mysanjeevni.mysanjeevni.features.medicines.domain.useCase

import com.mysanjeevni.mysanjeevni.features.medicines.domain.repository.MedicineRepository
import javax.inject.Inject

class GetMedicineByIdUseCase @Inject constructor(
    private val repository: MedicineRepository
) {

    suspend operator fun invoke(
        id: String
    ) = repository.getMedicineById(id)
}