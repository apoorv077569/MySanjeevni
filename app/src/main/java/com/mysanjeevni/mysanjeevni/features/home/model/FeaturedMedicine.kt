package com.mysanjeevni.mysanjeevni.features.home.model

data class FeaturedMedicine(
    val id: String,
    val name: String,
    val price: String,
    val mrp: String,
    val imageRes: String,
    val brand: String,
    val category: String,
    val subcategory: String,
    val productType: String,
    val inStock: Boolean,
    val description: String,
    val quantity: Int,
    val quantityUnit: String,
    val isPopularHomeopathy: Boolean,
    val isPopularGeneric: Boolean,
    val requiresPrescription: Boolean
)