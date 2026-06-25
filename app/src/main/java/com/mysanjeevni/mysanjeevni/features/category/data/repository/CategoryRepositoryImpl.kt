package com.mysanjeevni.mysanjeevni.features.category.data.repository

import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.category.domain.model.Category
import com.mysanjeevni.mysanjeevni.features.category.domain.repository.CategoryRepository
import com.mysanjeevni.mysanjeevni.features.category.data.mapper.toDomain
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val api: ApiService
) : CategoryRepository {

    override suspend fun getCategories(): List<Category> {
        val response = api.getCategories()
        if (response.isSuccessful) {
            return response.body()
                ?.tree
                ?.map { dto -> dto.toDomain() }
                ?: emptyList()
        }
        return emptyList()
    }
}