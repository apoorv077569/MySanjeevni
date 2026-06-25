package com.mysanjeevni.mysanjeevni.features.review.domain.usecase


import com.mysanjeevni.mysanjeevni.features.review.domain.repository.ReviewRepository
import javax.inject.Inject

class AddReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {

    suspend operator fun invoke(
        userId: String,
        productId: String,
        rating: Int,
        title: String,
        comment: String,
        userName: String
    ) = repository.addReview(
        userId = userId,
        productId = productId,
        rating = rating,
        title = title,
        comment = comment,
        userName = userName
    )
}