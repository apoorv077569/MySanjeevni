package com.mysanjeevni.mysanjeevni.features.category.data.dto

import com.google.gson.annotations.SerializedName

data class CategoryResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("tree")
    val tree: List<CategoryDto>

)