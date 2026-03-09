package com.mysanjeevni.mysanjeevni.features.health.domain.model
data class HealthArticle(
    val title: String,
    val category: String,
    val imgUrl: String,
    val readTime: String
)

data class HealthTool(
    val name: String,
    val iconUrl: String
)
