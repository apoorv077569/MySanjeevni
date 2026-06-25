package com.mysanjeevni.mysanjeevni.features.review.domain.usecase

import com.mysanjeevni.mysanjeevni.features.review.domain.repository.ReviewRepository
import javax.inject.Inject

class UpdateReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(
        reviewId: String,
        userId: String,
        rating: Int,
        title: String,
        comment: String
    ) = repository.updateReview(
        reviewId,
        userId,
        rating,
        title,
        comment
    )
}