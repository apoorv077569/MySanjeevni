package com.mysanjeevni.mysanjeevni.features.profile.data.dto

import com.google.gson.annotations.SerializedName

data class AddressResponseDto(

    @SerializedName("message")
    val message: String?,

    @SerializedName("total")
    val total: Int?,

    @SerializedName("addresses")
    val addresses: List<AddressDto>?
)