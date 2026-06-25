package com.mysanjeevni.mysanjeevni.features.labs.presentation.state


data class LabFilterState(
    val category: String = "All",
    val maxPrice: Float = 10000f
)
