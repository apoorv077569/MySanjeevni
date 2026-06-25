package com.mysanjeevni.mysanjeevni.features.medicines.domain.repository

import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine

interface MedicineRepository {

    suspend fun getMedicines(): Result<List<Medicine>>

    suspend fun getMedicineById(
        id: String
    ): Result<Medicine>
}