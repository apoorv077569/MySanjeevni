package com.mysanjeevni.mysanjeevni.features.review.data.dto

import com.google.gson.annotations.SerializedName

data class ReviewDto(
    @SerializedName("_id")
    val id: String?,
    val userId: String,
    val productId: String,
    val rating: Int,
    val title: String,
    val comment: String,
    val userName: String
)