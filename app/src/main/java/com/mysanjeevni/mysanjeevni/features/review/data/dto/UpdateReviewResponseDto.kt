package com.mysanjeevni.mysanjeevni.features.review.data.dto

data class UpdateReviewResponseDto(
    val message: String,
    val review: ReviewDto,
    val updatedRating: Double
)