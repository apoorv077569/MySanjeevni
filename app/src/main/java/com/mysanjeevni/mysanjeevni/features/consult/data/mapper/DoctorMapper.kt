package com.mysanjeevni.mysanjeevni.features.consult.data.mapper


import com.mysanjeevni.mysanjeevni.features.consult.data.dto.DoctorDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.DoctorResponseDto
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Doctor

fun DoctorDto.toDomain(): Doctor {
    return Doctor(
        id = id,
        name = name.orEmpty(),
        department = department.orEmpty(),
        specialization = specialization.orEmpty(),
        experience = experience ?: 0.0,
        consultationFee = consultationFee,
        rating = rating?:0.0,
        totalReviews = totalReviews,
        avatar = avatar.orEmpty(),
        availableDates = availableDates.orEmpty(),
        isAvailable = isAvailable,
        timeSlots = timeSlots.orEmpty()

    )
}

fun DoctorResponseDto.toDomain(): List<Doctor> {
    return doctors.map { it.toDomain() }
}