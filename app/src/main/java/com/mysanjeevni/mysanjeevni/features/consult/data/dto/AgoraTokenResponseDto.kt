package com.mysanjeevni.mysanjeevni.features.consult.data.dto

data class AgoraTokenResponseDto(
    val appId:String,
    val token:String,
    val uid:Int,
    val expiresIn:Int
)

data class AgoraErrorResponseDto(
    val error: String
)