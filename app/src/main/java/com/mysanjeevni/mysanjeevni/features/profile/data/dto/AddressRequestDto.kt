package com.mysanjeevni.mysanjeevni.features.profile.data.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateAddressRequestDto(

    @Expose
    val userId: String,

    @Expose
    val type: String,

    @Expose
    val fullName: String,

    @Expose
    val phone: String,

    @Expose
    val addressLine1: String,

    @Expose
    val addressLine2: String? = null,

    @Expose
    val city: String,

    @Expose
    val state: String,

    @Expose
    val pincode: String,

    @Expose
    val isDefault: Boolean = false
)

data class UpdateAddressRequestDto(

    @SerializedName("_id")
    @Expose
    val id: String,

    @Expose
    val userId: String,

    @Expose
    val type: String,

    @Expose
    val fullName: String,

    @Expose
    val phone: String,

    @Expose
    val addressLine1: String,

    @Expose
    val addressLine2: String? = null,

    @Expose
    val city: String,

    @Expose
    val state: String,

    @Expose
    val pincode: String,

    @Expose
    val isDefault: Boolean = false
)