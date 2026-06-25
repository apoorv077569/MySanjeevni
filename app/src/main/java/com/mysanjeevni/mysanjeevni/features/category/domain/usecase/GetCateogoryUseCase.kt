package com.mysanjeevni.mysanjeevni.features.category.domain.usecase

import com.mysanjeevni.mysanjeevni.features.category.data.repository.CategoryRepositoryImpl
import com.mysanjeevni.mysanjeevni.features.category.domain.model.Category
import com.mysanjeevni.mysanjeevni.features.category.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {

    suspend operator fun invoke(): List<Category> {
        return repository.getCategories()
    }
}