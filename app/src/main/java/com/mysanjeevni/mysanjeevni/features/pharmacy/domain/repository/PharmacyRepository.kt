package com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository

import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine


interface PharmacyRepository {
    suspend fun getMedicines(page:Int,limit:Int): List<Medicine>

}