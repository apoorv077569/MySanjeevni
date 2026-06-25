package com.mysanjeevni.mysanjeevni.features.review.data.dto

data class CreateReviewRequestDto(
    val userId: String,
    val productId: String,
    val rating: Int,
    val title: String,
    val comment: String,
    val userName: String
)