package com.mysanjeevni.mysanjeevni.features.pharmacy.data.repository

import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.medicines.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository.PharmacyRepository
import javax.inject.Inject

class MockPharmacyRepositoryImpl @Inject constructor(
    private val api: ApiService
) : PharmacyRepository {

    override suspend fun getMedicines(
        page:Int,
        limit:Int
    ): List<Medicine> {
        val response = api.getMedicines(page,limit)

        if (response.isSuccessful) {
            return response.body()?.products?.map {   // ✅ data → products
                it.toDomain()
            } ?: emptyList()
        } else {
            throw Exception("API Error")
        }
    }
}