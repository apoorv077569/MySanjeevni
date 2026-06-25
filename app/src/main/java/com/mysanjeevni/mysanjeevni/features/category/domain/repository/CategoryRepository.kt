package com.mysanjeevni.mysanjeevni.features.category.domain.repository

import com.mysanjeevni.mysanjeevni.features.category.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}