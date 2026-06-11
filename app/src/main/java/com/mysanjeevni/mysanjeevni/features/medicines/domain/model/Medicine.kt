package com.mysanjeevni.mysanjeevni.features.medicines.domain.model

data class Medicine(

    val id: String,

    val name: String,

    val description: String,

    val price: Double,

    val mrp: Double,

    val category: String,

    val diseaseCategory: String,

    val diseaseSubcategory: String,

    val productType: String,

    val brand: String,

    val stock: Int,

    val quantity: Int,

    val quantityUnit: String,

    val image: String,

    val images: List<String>,

    val specifications: String,

    val safetyInformation: String,

    val requiresPrescription: Boolean,

    val vendorName: String,

    val vendorRating: Double,

    val rating: Double,

    val reviews: Int
)