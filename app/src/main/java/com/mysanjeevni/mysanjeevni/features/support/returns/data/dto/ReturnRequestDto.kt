package com.mysanjeevni.mysanjeevni.features.support.returns.data.dto

data class ReturnRequestDto(
    val userId: String,
    val userName: String,
    val userEmail: String,
    val orderId: String,
    val productName: String,
    val reason: String,
    val preferredResolution: String
)