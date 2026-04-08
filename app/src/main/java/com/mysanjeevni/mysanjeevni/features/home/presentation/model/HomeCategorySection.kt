package com.mysanjeevni.mysanjeevni.features.home.presentation.model

data class HomeCategorySection(
    val id: String,
    val title: String,
    val items: List<HomeCategoryItem>
)
data class HomeCategoryItem(
    val id: String,
    val title: String,
    val imageUrl: Int,
    val type: HomeCategoryType
)

