package com.mysanjeevni.mysanjeevni.features.pharmacy.domain.usecase

import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.model.MedicinePage
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository.PharmacyRepository
import javax.inject.Inject

class GetMedicineUseCase @Inject constructor(
    private val repository: PharmacyRepository
) {
    suspend operator fun invoke(
        page:Int,
        limit:Int,
        category:String? = null
    ): MedicinePage{
        return repository.getMedicines(page=page,limit=limit,category = category);
    }
}