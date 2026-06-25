package com.mysanjeevni.mysanjeevni.features.labs.domain.model


data class Serviceability(
    val provider: String,
    val pincode: String,
    val isServiceable: Boolean,
    val serviceTypes: List<String>
)