package com.mysanjeevni.mysanjeevni.features.pharmacy.data.remote

import com.mysanjeevni.mysanjeevni.features.pharmacy.data.remote.dto.MedicineDto
import retrofit2.http.GET

interface PharmacyApi {
    @GET("products")
    suspend fun getMedicines(): List<MedicineDto>
}