package com.mysanjeevni.mysanjeevni.data.remote.model.address

import com.google.gson.annotations.SerializedName

data class AddressModel(

    @SerializedName("_id")
    val id: String? = "",

    val userId: String = "",

    val type: String = "",

    val fullName: String = "",

    val phone: String = "",

    val addressLine1: String = "",

    val addressLine2: String = "",

    val city: String = "",

    val state: String = "",

    val pincode: String = "",

    val country: String = "India",

    val isDefault: Boolean = false,

    val createdAt: String = "",

    val updatedAt: String = ""
)
fun AddressModel.toCreateRequest(): CreateAddressRequest {
    return CreateAddressRequest(
        userId = userId,
        type = type,
        fullName = fullName,
        phone = phone,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        city = city,
        state = state,
        pincode = pincode,
        isDefault = isDefault
    )
}

fun AddressModel.toUpdateRequest(): UpdateAddressRequest {
    return UpdateAddressRequest(
        id = id ?: throw IllegalArgumentException("Update requires non-null id"),
        userId = userId,
        type = type,
        fullName = fullName,
        phone = phone,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        city = city,
        state = state,
        pincode = pincode,
        isDefault = isDefault
    )
}

