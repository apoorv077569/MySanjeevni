package com.mysanjeevni.mysanjeevni.data.remote.model.address

import com.google.gson.annotations.Expose

data class CreateAddressRequest(
    @Expose val userId: String,
    @Expose val type: String,
    @Expose val fullName: String,
    @Expose val phone: String,
    @Expose val addressLine1: String,
    @Expose val addressLine2: String? = null,
    @Expose val city: String,
    @Expose val state: String,
    @Expose val pincode: String,
    @Expose val isDefault: Boolean = false
)