package com.mysanjeevni.mysanjeevni.features.auth.data.mapper

import com.mysanjeevni.mysanjeevni.data.remote.model.user.User
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.AuthResponseDto
import com.mysanjeevni.mysanjeevni.features.auth.domain.model.AuthResult
import com.mysanjeevni.mysanjeevni.features.auth.domain.model.AuthUser

fun User.toDomain(): AuthUser {
    return AuthUser(
        id = _id.orEmpty(),
        fullName = fullName.orEmpty(),
        phone = phone.orEmpty(),
        role = role.orEmpty(),
        email = email.orEmpty(),
        profileImage = profileImage,
        address = address.orEmpty(),
        isVerified = isVerified ?: false
    )
}

fun AuthResponseDto.toDomain(): AuthResult {
    return AuthResult(
        message = message.orEmpty(),
        token = token,
        phoneVerificationToken = phoneVerificationToken,
        user = user?.toDomain(),

    )
}