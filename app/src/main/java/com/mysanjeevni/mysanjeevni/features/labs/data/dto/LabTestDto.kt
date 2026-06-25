package com.mysanjeevni.mysanjeevni.features.labs.data.dto

import com.google.gson.annotations.SerializedName

data class LabTestDto(
    @SerializedName("_id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("price") val price: Int?,
    @SerializedName("mrp") val mrp: Int?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("reportTime") val reportTime: String?,
    @SerializedName("fasting") val fasting: Boolean?,
    @SerializedName("homeCollectionAvailable") val homeCollectionAvailable: Boolean?,
    @SerializedName("testsIncluded") val testsIncluded: List<String>?
)
