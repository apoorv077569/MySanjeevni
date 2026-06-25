package com.mysanjeevni.mysanjeevni.features.review.domain.model

data class Review(
    val id: String?,
    val userId: String,
    val productId: String,
    val rating: Int,
    val title: String,
    val comment: String,
    val userName: String
)