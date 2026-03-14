package com.mysanjeevni.mysanjeevni.features.profile.presentation.state

data class AddressState(
    val isLoading : Boolean = false,
    val addresses : List<AddressItem> = emptyList(),
    val error:String?=null
)
data class AddressItem(
    val id: String,
    val type:String,
    val name:String,
    val phoneNumber: String,
    val addressLine:String,
    val cityStateZip: String,
    val isDefault: Boolean = false
)
