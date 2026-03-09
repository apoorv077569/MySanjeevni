package com.mysanjeevni.mysanjeevni.features.consult.domnain.model

data class Doctor(
    val id:Int,
    val name: String,
    val specialty:String,
    val experience:String,
    val rating: Double,
    val reviewCount:Int,
    val fee:Double,
    val availableTime:String,
    val imageUrl:String
)
data class Specialty(
    val name: String,
    val imageUrl: String
)
