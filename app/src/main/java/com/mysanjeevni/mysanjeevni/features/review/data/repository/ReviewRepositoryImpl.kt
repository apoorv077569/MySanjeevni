package com.mysanjeevni.mysanjeevni.features.review.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.review.data.dto.CreateReviewRequestDto
import com.mysanjeevni.mysanjeevni.features.review.data.dto.UpdateReviewRequestDto
import com.mysanjeevni.mysanjeevni.features.review.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.review.domain.model.Review
import com.mysanjeevni.mysanjeevni.features.review.domain.repository.ReviewRepository
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val sessionManager: SessionManager
) : ReviewRepository {

    override suspend fun getReviews(
        productId: String
    ): Result<List<Review>> {

        return try {
            val response = api.getReviews(productId)
            if (response.isSuccessful) {
                val reviews = response.body()?.reviews
                        ?.map { it.toDomain() }
                        .orEmpty()

                Result.success(reviews)

            } else {

                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Failed to fetch reviews"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun addReview(
        userId: String,
        productId: String,
        rating: Int,
        title: String,
        comment: String,
        userName: String
    ): Result<Review> {

        return try {

            Log.d("REVIEW_DEBUG", "========== ADD REVIEW ==========")
            Log.d("REVIEW_DEBUG", "userId = $userId")
            Log.d("REVIEW_DEBUG", "productId = $productId")
            Log.d("REVIEW_DEBUG", "rating = $rating")
            Log.d("REVIEW_DEBUG", "title = $title")
            Log.d("REVIEW_DEBUG", "comment = $comment")
            Log.d("REVIEW_DEBUG", "userName = $userName")

            val request = CreateReviewRequestDto(
                userId = userId,
                productId = productId,
                rating = rating,
                title = title,
                comment = comment,
                userName = userName
            )

            Log.d("REVIEW_DEBUG", "REQUEST = $request")

            val response = api.createReview(
                "Bearer ${sessionManager.getToken()}",
                request
            )

            Log.d(
                "REVIEW_DEBUG",
                "HTTP_CODE = ${response.code()}"
            )

            Log.d(
                "REVIEW_DEBUG",
                "IS_SUCCESSFUL = ${response.isSuccessful}"
            )

            if (response.isSuccessful) {

                Log.d(
                    "REVIEW_DEBUG",
                    "BODY = ${response.body()}"
                )

                val review = response.body()
                    ?.review
                    ?.toDomain()

                Log.d(
                    "REVIEW_DEBUG",
                    "MAPPED_REVIEW = $review"
                )

                if (review != null) {

                    Log.d(
                        "REVIEW_DEBUG",
                        "ADD REVIEW SUCCESS"
                    )

                    Result.success(review)

                } else {

                    Log.e(
                        "REVIEW_DEBUG",
                        "Review object is null"
                    )

                    Result.failure(
                        Exception("Review not found")
                    )
                }

            } else {

                val errorBody =
                    response.errorBody()?.string()

                Log.e(
                    "REVIEW_DEBUG",
                    "ERROR_BODY = $errorBody"
                )

                Result.failure(
                    Exception(
                        errorBody
                            ?: "Failed to add review"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                "REVIEW_DEBUG",
                "EXCEPTION = ${e.message}",
                e
            )

            Result.failure(e)
        }
    }

    override suspend fun updateReview(
        reviewId: String,
        userId: String,
        rating: Int,
        title: String,
        comment: String
    ): Result<Review> {

        return try {

            val response = api.updateReview(
                reviewId,
                UpdateReviewRequestDto(
                    userId,
                    rating,
                    title,
                    comment
                )
            )

            if (response.isSuccessful) {

                Result.success(
                    response.body()!!
                        .review
                        .toDomain()
                )

            } else {

                Result.failure(
                    Exception("Failed to update review")
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

}