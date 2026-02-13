package com.mysanjeevni.mysanjeevni.features.pharmacy.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MedicineDto(
    @SerializedName("id") val id:Int,
    @SerializedName("title") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("description") val description:String,
    @SerializedName("image") val imageUrl: String
)
