package com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.components

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatPrescriptionDate(
    date: String
): String {

    if (date.isBlank()) {
        return "—"
    }

    return try {

        // API date is in UTC because it ends with Z
        val input = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.US
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        // Display date in IST
        val output = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.US
        ).apply {
            timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        }

        val parsed = input.parse(date)

        if (parsed != null) {
            output.format(parsed)
        } else {
            date
        }

    } catch (e: Exception) {
        date
    }
}