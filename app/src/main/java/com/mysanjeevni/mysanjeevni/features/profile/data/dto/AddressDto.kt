package com.mysanjeevni.mysanjeevni.features.profile.data.dto

import com.google.gson.annotations.SerializedName

data class AddressDto(

    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("fullName")
    val fullName: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("addressLine1")
    val addressLine1: String? = null,

    @SerializedName("addressLine2")
    val addressLine2: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("pincode")
    val pincode: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("isDefault")
    val isDefault: Boolean? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
)