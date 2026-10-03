package com.mysanjeevni.mysanjeevni.features.consult.data.mapper


import com.mysanjeevni.mysanjeevni.features.consult.data.dto.ConsultationDto
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Consultation
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.BookConsultationRequestDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.BookConsultationResponseDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.CancelConsultationResponseDto
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.BookConsultationRequest
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.BookConsultationResponse
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.CancelConsultationResponse

fun ConsultationDto.toDomain(): Consultation {

    return Consultation(

        id = id.orEmpty(),

        userId = userId.orEmpty(),

        doctorId = doctorId.orEmpty(),

        patientName = patientName.orEmpty(),

        patientPhone = patientPhone.orEmpty(),

        patientEmail = patientEmail.orEmpty(),

        doctorName = doctorName
            ?.takeIf { it.isNotBlank() }
            ?: "Doctor",

        specialization = doctorSpecialization
            ?.takeIf { it.isNotBlank() }
            ?: "General Physician",

        department = doctorDepartment
            ?.takeIf { it.isNotBlank() }
            ?: "General Medicine",

        appointmentDate = appointmentDate.orEmpty(),

        allottedTime = allottedTime
            ?.takeIf { it.isNotBlank() }
            ?: preferredTimeSlot.orEmpty(),

        consultationType = consultationType
            ?.takeIf { it.isNotBlank() }
            ?: "video",

        queueNumber = queueNumber ?: 0,

        patientsAhead = patientsAhead ?: 0,

        status = status
            ?.takeIf { it.isNotBlank() }
            ?: "unknown",

        fees = fees ?: 0.0,

        paymentStatus = paymentStatus
            ?.takeIf { it.isNotBlank() }
            ?: "pending",

        symptoms = symptoms.orEmpty(),

        notes = notes.orEmpty(),

        prescription = prescription
            ?.takeIf { it.isNotBlank() },

        feedback = feedback.orEmpty()
    )
}
fun BookConsultationRequest.toDto(): BookConsultationRequestDto {

    return BookConsultationRequestDto(
        userId = userId,
        doctorId = doctorId,
        patientName = patientName,
        patientPhone = patientPhone,
        patientEmail = patientEmail,
        appointmentDate = appointmentDate,
        consultationType = consultationType,
        symptoms = symptoms,
        razorpayOrderId = razorpayOrderId,
        razorpayPaymentId = razorpayPaymentId,
        razorpaySignature = razorpaySignature
    )
}
fun BookConsultationResponseDto.toDomain(): BookConsultationResponse {

    return BookConsultationResponse(
        consultation = consultation?.toDomain(),
        message = message.orEmpty()
    )
}

fun CancelConsultationResponseDto.toDomain(): CancelConsultationResponse {
    return CancelConsultationResponse(
        consultation = consultation?.toDomain(),
        refundId = refund?.id,
        refundStatus = refund?.status,
        message = message.orEmpty()
    )
}
