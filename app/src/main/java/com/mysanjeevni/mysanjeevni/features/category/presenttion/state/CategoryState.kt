package com.mysanjeevni.mysanjeevni.features.category.presenttion.state

import com.mysanjeevni.mysanjeevni.features.category.domain.model.Category

data class CategoryState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String? = null
)