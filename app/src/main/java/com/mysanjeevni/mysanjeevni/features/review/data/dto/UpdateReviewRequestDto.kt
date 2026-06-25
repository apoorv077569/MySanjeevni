package com.mysanjeevni.mysanjeevni.features.review.data.dto

data class UpdateReviewRequestDto(
    val userId: String,
    val rating: Int,
    val title: String,
    val comment: String
)