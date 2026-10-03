//package com.mysanjeevni.mysanjeevni.features.profile.data.mapper
//
//import com.mysanjeevni.mysanjeevni.features.profile.data.dto.AddressDto
//import com.mysanjeevni.mysanjeevni.features.profile.data.dto.CreateAddressRequestDto
//import com.mysanjeevni.mysanjeevni.features.profile.data.dto.UpdateAddressRequestDto
//import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Address
//import com.mysanjeevni.mysanjeevni.data.remote.model.address.AddressModel
//
//
//fun AddressDto.toDomain(): Address {
//    return Address(
//        id = id.orEmpty(),
//        userId = userId.orEmpty(),
//        type = type.orEmpty(),
//        fullName = fullName.orEmpty(),
//        phone = phone.orEmpty(),
//        addressLine1 = addressLine1.orEmpty(),
//        addressLine2 = addressLine2.orEmpty(),
//        city = city.orEmpty(),
//        state = state.orEmpty(),
//        pincode = pincode.orEmpty(),
//        country = country ?: "India",
//        isDefault = isDefault ?: false,
//        createdAt = createdAt.orEmpty(),
//        updatedAt = updatedAt.orEmpty()
//    )
//}
//
//fun Address.toCreateRequestDto(): CreateAddressRequestDto {
//    return CreateAddressRequestDto(
//        userId = userId,
//        type = type,
//        fullName = fullName,
//        phone = phone,
//        addressLine1 = addressLine1,
//        addressLine2 = addressLine2.ifBlank { null },
//        city = city,
//        state = state,
//        pincode = pincode,
//        isDefault = isDefault
//    )
//}
//
//fun Address.toUpdateRequestDto(): UpdateAddressRequestDto {
//    require(id.isNotBlank()) {
//        "Update requires non-empty address id"
//    }
//
//    return UpdateAddressRequestDto(
//        id = id,
//        userId = userId,
//        type = type,
//        fullName = fullName,
//        phone = phone,
//        addressLine1 = addressLine1,
//        addressLine2 = addressLine2.ifBlank { null },
//        city = city,
//        state = state,
//        pincode = pincode,
//        isDefault = isDefault
//    )
//}
//
//
//
//fun AddressModel.toDomain(): Address {
//    return Address(
//        id = id.orEmpty(),
//        userId = userId,
//        type = type,
//        fullName = fullName,
//        phone = phone,
//        addressLine1 = addressLine1,
//        addressLine2 = addressLine2,
//        city = city,
//        state = state,
//        pincode = pincode,
//        country = country,
//        isDefault = isDefault,
//        createdAt = createdAt,
//        updatedAt = updatedAt
//    )
//}
//
//fun Address.toAddressModel(): AddressModel {
//    return AddressModel(
//        id = id,
//        userId = userId,
//        type = type,
//        fullName = fullName,
//        phone = phone,
//        addressLine1 = addressLine1,
//        addressLine2 = addressLine2,
//        city = city,
//        state = state,
//        pincode = pincode,
//        country = country,
//        isDefault = isDefault,
//        createdAt = createdAt,
//        updatedAt = updatedAt
//    )
//}


package com.mysanjeevni.mysanjeevni.features.profile.data.mapper

import com.mysanjeevni.mysanjeevni.features.profile.data.dto.AddressDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.CreateAddressRequestDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.UpdateAddressRequestDto
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Address

fun AddressDto.toDomain(): Address {
    return Address(
        id = id.orEmpty(),
        userId = userId.orEmpty(),
        type = type.orEmpty(),
        fullName = fullName.orEmpty(),
        phone = phone.orEmpty(),
        addressLine1 = addressLine1.orEmpty(),
        addressLine2 = addressLine2.orEmpty(),
        city = city.orEmpty(),
        state = state.orEmpty(),
        pincode = pincode.orEmpty(),
        country = country ?: "India",
        isDefault = isDefault ?: false,
        createdAt = createdAt.orEmpty(),
        updatedAt = updatedAt.orEmpty()
    )
}

fun Address.toCreateRequestDto(): CreateAddressRequestDto {
    return CreateAddressRequestDto(
        userId = userId,
        type = type,
        fullName = fullName,
        phone = phone,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2.ifBlank { null },
        city = city,
        state = state,
        pincode = pincode,
        isDefault = isDefault
    )
}

fun Address.toUpdateRequestDto(): UpdateAddressRequestDto {
    require(id.isNotBlank()) {
        "Update requires non-empty address id"
    }

    return UpdateAddressRequestDto(
        id = id,
        userId = userId,
        type = type,
        fullName = fullName,
        phone = phone,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2.ifBlank { null },
        city = city,
        state = state,
        pincode = pincode,
        isDefault = isDefault
    )
}