package com.mysanjeevni.mysanjeevni.features.review.data.dto

data class ReviewListResponseDto(
    val message: String,
    val reviews: List<ReviewDto>,
    val total: Int,
    val averageRating: Double,
    val page: Int,
    val limit: Int,
    val hasMore: Boolean
)