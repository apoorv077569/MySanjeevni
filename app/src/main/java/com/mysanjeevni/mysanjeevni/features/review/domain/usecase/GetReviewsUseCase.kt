package com.mysanjeevni.mysanjeevni.features.review.domain.usecase

import com.mysanjeevni.mysanjeevni.features.review.domain.repository.ReviewRepository
import javax.inject.Inject

class GetReviewsUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(productId: String) = repository.getReviews(productId)
}