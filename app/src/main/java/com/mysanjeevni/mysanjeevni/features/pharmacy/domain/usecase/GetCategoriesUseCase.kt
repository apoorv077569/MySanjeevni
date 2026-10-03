package com.mysanjeevni.mysanjeevni.features.pharmacy.domain.usecase

import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository.PharmacyRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository : PharmacyRepository
) {
    suspend operator fun invoke():List<String>{
        return repository.getCategories()
    }
}