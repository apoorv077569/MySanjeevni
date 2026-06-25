package com.mysanjeevni.mysanjeevni.features.medicines.domain.useCase

import com.mysanjeevni.mysanjeevni.features.medicines.domain.repository.MedicineRepository
import javax.inject.Inject

class GetMedicinesUseCase @Inject constructor(
    private val repository: MedicineRepository
) {

    suspend operator fun invoke() =
        repository.getMedicines()
}