package com.mysanjeevni.mysanjeevni.features.consult.data.mapper

import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Consultation
import com.mysanjeevni.mysanjeevni.features.consult.presentation.state.ConsultItem
import com.mysanjeevni.mysanjeevni.features.consult.presentation.state.ConsultStatus

fun Consultation.toConsultItem(): ConsultItem {

    val uiStatus = when {

        status.equals("pending", ignoreCase = true) ||
                status.equals("confirmed", ignoreCase = true) -> {
            ConsultStatus.SCHEDULED
        }

        status.equals("completed", ignoreCase = true) -> {
            ConsultStatus.COMPLETED
        }

        status.equals("cancelled", ignoreCase = true) -> {
            ConsultStatus.CANCELLED
        }

        else -> {
            ConsultStatus.SCHEDULED
        }
    }

    return ConsultItem(
        id = id,

        doctorName = doctorName,

        // For UI, department is better fallback
        // when specialization is empty.
        specialization = specialization
            .takeIf { it.isNotBlank() }
            ?: department,

        hospitalName = null,

        date = formatDate(appointmentDate),

        time = allottedTime,

        status = uiStatus,

        isVideoCall = consultationType.equals(
            "video",
            ignoreCase = true
        ),

        // NEW
        symptoms = symptoms,

        // NEW
        fees = fees
    )
}

private fun formatDate(
    date: String
): String {

    return try {

        val input = java.text.SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            java.util.Locale.US
        )

        input.timeZone = java.util.TimeZone.getTimeZone("UTC")

        val output = java.text.SimpleDateFormat(
            "EEE, dd MMM yyyy",
            java.util.Locale.US
        )

        val parsed = input.parse(date)

        if (parsed != null) {
            output.format(parsed)
        } else {
            date
        }

    } catch (_: Exception) {
        date
    }
}