package com.mysanjeevni.mysanjeevni.features.medicines.data.repository

import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.medicines.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.medicines.domain.repository.MedicineRepository
import javax.inject.Inject

class MedicineRepositoryImpl @Inject constructor(
    private val api: ApiService
) : MedicineRepository {

    override suspend fun getMedicines(): Result<List<Medicine>> {
        return try {
            val response = api.getMedicines()
            if (response.isSuccessful) {
                val medicines = response.body()?.products?.map { it.toDomain() }.orEmpty()
                Result.success(medicines)
            }
            else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to fetch medicines"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getMedicineById(
        id: String
    ): Result<Medicine> {
        return try {
            val response = api.getMedicineById(id)
            if (response.isSuccessful) {
                val medicine = response.body()?.product?.toDomain()
                if (medicine != null) {
                    Result.success(medicine)
                } else {
                    Result.failure(Exception("Medicine not found"))
                }
            }
            else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to fetch medicine"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}