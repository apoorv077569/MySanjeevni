package com.mysanjeevni.mysanjeevni.features.category.data.dto

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("_id")
    val id: String,

    val name: String,

    val children: List<CategoryDto> = emptyList()
)