package com.mysanjeevni.mysanjeevni.features.labs.domain.model

data class LabTestDetail(
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
    val testsIncluded:List<String>
)
