package com.mysanjeevni.mysanjeevni.features.review.domain.repository

import com.mysanjeevni.mysanjeevni.features.review.domain.model.Review

interface ReviewRepository {
    suspend fun getReviews(productId: String): Result<List<Review>>

    suspend fun addReview(
        userId: String,
        productId: String,
        rating: Int,
        title: String,
        comment: String,
        userName: String
    ): Result<Review>

    suspend fun updateReview(
        reviewId: String,
        userId: String,
        rating: Int,
        title: String,
        comment: String
    ): Result<Review>

}