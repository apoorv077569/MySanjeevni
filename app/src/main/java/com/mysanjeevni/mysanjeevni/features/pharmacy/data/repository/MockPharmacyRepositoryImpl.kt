package com.mysanjeevni.mysanjeevni.features.pharmacy.data.repository

import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.category.data.dto.CategoryDto
import com.mysanjeevni.mysanjeevni.features.medicines.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.model.MedicinePage
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository.PharmacyRepository
import javax.inject.Inject

class MockPharmacyRepositoryImpl @Inject constructor(
    private val api: ApiService
) : PharmacyRepository {

    override suspend fun getMedicines(
        page: Int,
        limit: Int,
        category: String?
    ): MedicinePage {

        val response = api.getMedicines(page, limit,category)

        if (response.isSuccessful) {

            val body = response.body()
                ?: throw Exception("Empty API response")

            return MedicinePage(
                medicines = body.products.map { it.toDomain() },
                total = body.pagination.total ?: 0,
                totalPages = body.pagination.pages ?: 1
            )

        } else {
            throw Exception("API Error: ${response.code()}")
        }
    }

    private fun flattenCategories(
        categories: List<CategoryDto>
    ): List<String> {

        val result = mutableListOf<String>()

        fun addCategory(category: CategoryDto) {

            if (category.isActive && category.name.isNotBlank()) {
                result.add(category.name)
            }

            category.children.forEach { child ->
                addCategory(child)
            }
        }

        categories.forEach { category ->
            addCategory(category)
        }

        return result.distinct()
    }

    override suspend fun getCategories(): List<String> {

        val response = api.getCategories()
        if (response.isSuccessful) {

            val body = response.body()

            return if (body?.success == true) {
                flattenCategories(body.tree)
            } else {
                emptyList()
            }

        } else {
            throw Exception("Categories API Error: ${response.code()}")
        }
    }
}