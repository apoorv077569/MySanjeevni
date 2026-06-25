package com.mysanjeevni.mysanjeevni.features.review.data.mapper

import com.mysanjeevni.mysanjeevni.features.review.data.dto.ReviewDto
import com.mysanjeevni.mysanjeevni.features.review.domain.model.Review

fun ReviewDto.toDomain(): Review {

    return Review(
        id = id,
        userId = userId,
        productId = productId,
        rating = rating,
        title = title,
        comment = comment,
        userName = userName
    )
}