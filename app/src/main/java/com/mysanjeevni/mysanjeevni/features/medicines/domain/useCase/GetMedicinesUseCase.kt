package com.mysanjeevni.mysanjeevni.features.medicines.domain.useCase

import com.mysanjeevni.mysanjeevni.features.medicines.domain.repository.MedicineRepository
import javax.inject.Inject

class GetMedicinesUseCase @Inject constructor(
    private val repository: MedicineRepository
) {

    suspend operator fun invoke(page:Int,limit:Int) =
        repository.getMedicines(page = page,limit = limit)
}