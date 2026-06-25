package com.mysanjeevni.mysanjeevni.features.labs.domain.model

data class LabTest(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val mrp: Int,
    val icon: String,
    val category: String,
    val rating: Double,
    val reportTime: String,
    val fasting: Boolean,
    val testCount: Int,
    val homeCollectionAvailable: Boolean
)