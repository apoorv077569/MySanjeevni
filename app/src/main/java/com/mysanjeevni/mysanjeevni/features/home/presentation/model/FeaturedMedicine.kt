package com.mysanjeevni.mysanjeevni.features.home.presentation.model

import androidx.annotation.DrawableRes

data class FeaturedMedicine(
    val id:Int,
    val name: String,
    val price:String,
    @DrawableRes val imageRes: Int
)
