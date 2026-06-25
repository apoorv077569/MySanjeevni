package com.mysanjeevni.mysanjeevni.features.category.data.mapper

import com.mysanjeevni.mysanjeevni.features.category.data.dto.CategoryDto
import com.mysanjeevni.mysanjeevni.features.category.domain.model.Category

fun CategoryDto.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        children = children.map { it.toDomain() }
    )
}