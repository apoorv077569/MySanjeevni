package com.mysanjeevni.mysanjeevni.features.category.data.dto

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val parentId: String?,
    val sortOrder:Int,
    val isActive: Boolean,
    val productCount:Int = 0,
    val children: List<CategoryDto> = emptyList()
)