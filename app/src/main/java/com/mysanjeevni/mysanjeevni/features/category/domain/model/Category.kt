package com.mysanjeevni.mysanjeevni.features.category.domain.model

data class Category(
    val id: String,
    val name: String,
    val children: List<Category>
)