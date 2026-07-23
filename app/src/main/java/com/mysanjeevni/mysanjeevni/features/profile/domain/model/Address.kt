package com.mysanjeevni.mysanjeevni.features.profile.domain.model

data class Address(
    val id: String,
    val userId: String,
    val type: String,
    val fullName: String,
    val phone: String,
    val addressLine1: String,
    val addressLine2: String,
    val city: String,
    val state: String,
    val pincode: String,
    val country: String,
    val isDefault: Boolean,
    val createdAt: String,
    val updatedAt: String
)