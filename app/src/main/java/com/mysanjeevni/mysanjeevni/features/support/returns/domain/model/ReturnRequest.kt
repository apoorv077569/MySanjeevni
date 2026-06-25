package com.mysanjeevni.mysanjeevni.features.support.returns.domain.model

data class ReturnRequest(
    val id: String,
    val orderId: String,
    val productName: String,
    val reason: String,
    val preferredResolution: String,
    val status: String,
    val supportNote: String
)