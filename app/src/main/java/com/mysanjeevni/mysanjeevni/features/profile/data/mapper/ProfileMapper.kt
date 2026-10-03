package com.mysanjeevni.mysanjeevni.features.profile.data.mapper

import com.mysanjeevni.mysanjeevni.features.profile.data.dto.UpdateProfileRequestDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.UpdateProfileResponseDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.UserDto
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.ProfileUpdate
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.UserProfile

fun UserDto.toDomain(): UserProfile {
    return UserProfile(
        id = id.orEmpty(),
        fullName = fullName.orEmpty(),
        phone = phone.orEmpty(),
        role = role.orEmpty(),
        email = email.orEmpty(),
        profileImage = profileImage,
        address = address.orEmpty(),
        isVerified = isVerified ?: false
    )
}

fun ProfileUpdate.toRequestDto(): UpdateProfileRequestDto {
    return UpdateProfileRequestDto(
        userId = userId,
        fullName = fullName,
        phone = phone,
        fullAddress = fullAddress,
        profileImage = profileImage
    )
}

fun UpdateProfileResponseDto.toDomain(): UserProfile? {
    return user?.toDomain()
}