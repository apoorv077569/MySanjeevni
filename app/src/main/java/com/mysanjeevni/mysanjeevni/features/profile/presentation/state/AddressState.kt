package com.mysanjeevni.mysanjeevni.features.profile.presentation.state

import android.location.Address
import com.google.gson.annotations.SerializedName

data class AddressState(
    val isLoading: Boolean = false,
    val addresses: List<AddressItem> = emptyList(),
    val selectedAddress: AddressItem? = null,
    val error: String? = null,
)

data class AddressItem(

    @SerializedName("_id")
    val id: String,

    val userId: String,

    val type: String,

    val fullName: String,

    val phone: String,

    val addressLine1: String,

    val addressLine2: String,

    val city: String,

    val state: String,

    val pincode: String,

    val country: String,

    val isDefault: Boolean,

    val createdAt: String,

    val updatedAt: String
)
data class AddressResponse(
    val message: String,
    val addresses: List<AddressItem>,
    val total: Int
)