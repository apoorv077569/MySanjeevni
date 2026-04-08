package com.mysanjeevni.mysanjeevni.features.home.presentation.model

data class QuickCategory(
    val name: String,
    val iconUrl:String,
    val isNew: Boolean = false
)

data class GridCategory(
    val name: String,
    val imageUrl:String,
    val type: HomeCategoryType
)
