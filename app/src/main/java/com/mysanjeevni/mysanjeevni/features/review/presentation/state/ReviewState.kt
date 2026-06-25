package com.mysanjeevni.mysanjeevni.features.review.presentation.state

import com.mysanjeevni.mysanjeevni.features.review.domain.model.Review


data class ReviewState(

    val isLoading: Boolean = false,

    val reviews: List<Review> = emptyList(),

    val error: String? = null
)