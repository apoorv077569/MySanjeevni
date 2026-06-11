package com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository

import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine


interface PharmacyRepository {
    suspend fun getMedicines(): List<Medicine>

}