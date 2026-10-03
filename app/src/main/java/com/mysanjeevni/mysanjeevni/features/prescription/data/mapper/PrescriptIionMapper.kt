package com.mysanjeevni.mysanjeevni.features.prescription.data.mapper

import com.mysanjeevni.mysanjeevni.features.prescription.data.dto.MedicineDto
import com.mysanjeevni.mysanjeevni.features.prescription.data.dto.PrescriptionDto
import com.mysanjeevni.mysanjeevni.features.prescription.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.prescription.domain.model.Prescription


fun PrescriptionDto.toDomain(): Prescription {

    return Prescription(
        id = id.orEmpty(),

        doctorName = doctorName
            ?.takeIf { it.isNotBlank() }
            ?: "Doctor",

        doctorRegistrationNumber =
            doctorRegistrationNumber.orEmpty(),

        doctorAddress =
            doctorAddress.orEmpty(),

        hospitalName =
            hospitalName?.takeIf {
                it.isNotBlank()
            },

        issueDate =
            issueDate.orEmpty(),

        expiryDate =
            expiryDate.orEmpty(),

        medicines =
            medicines
                .orEmpty()
                .map {
                    it.toDomain()
                },

        diagnosis =
            diagnosis.orEmpty(),

        notes =
            notes.orEmpty(),

        status =
            status
                ?.takeIf { it.isNotBlank() }
                ?: "active",

        isVerified =
            isVerified ?: false,

        consultationId =
            consultationId?.id,

        appointmentDate =
            consultationId?.appointmentDate,

        allottedTime =
            consultationId?.allottedTime,

        consultationType =
            consultationId?.consultationType,

        symptoms =
            consultationId?.symptoms
    )
}


fun MedicineDto.toDomain(): Medicine {

    return Medicine(
        id = id.orEmpty(),

        name =
            name
                ?.takeIf { it.isNotBlank() }
                ?: "Medicine",

        dosage =
            dosage.orEmpty(),

        frequency =
            frequency.orEmpty(),

        duration =
            duration.orEmpty()
    )
}