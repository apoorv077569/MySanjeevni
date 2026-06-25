package com.mysanjeevni.mysanjeevni.features.labs.data.dto

import com.google.gson.annotations.SerializedName

data class LabTestDetailDto(
    @SerializedName("_id")
    val id:String,
    val name:String,
    val description:String,
    val price: Double,
    val mrp: Double,
    val category:String,
    val rating:Double,
    val reportTime:String,
    val sampleType:String,
    val fasting: Boolean,
    val fastingHour:Int,
    val homeCollectionAvailable: Boolean,
    @SerializedName("testsIncluded")
    val testsIncluded: List<String> = emptyList()
    )
