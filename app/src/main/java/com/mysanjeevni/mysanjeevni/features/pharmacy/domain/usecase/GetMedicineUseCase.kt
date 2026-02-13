package com.mysanjeevni.mysanjeevni.features.pharmacy.domain.usecase

import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository.PharmacyRepository
import javax.inject.Inject

class GetMedicineUseCase @Inject constructor(
    private val repository: PharmacyRepository
) {
    suspend operator fun invoke(): List<Medicine>{
        return repository.getMedicines();
    }
}