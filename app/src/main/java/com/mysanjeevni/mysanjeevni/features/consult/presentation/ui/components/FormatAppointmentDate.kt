package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components

fun formatAppointmentDate(
    date: String
): String {

    return try {

        val inputFormats = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss"
        )

        val parsedDate = inputFormats
            .asSequence()
            .mapNotNull { pattern ->

                try {

                    val formatter =
                        java.text.SimpleDateFormat(
                            pattern,
                            java.util.Locale.US
                        )

                    formatter.timeZone =
                        java.util.TimeZone.getTimeZone("UTC")

                    formatter.parse(date)

                } catch (_: Exception) {
                    null
                }
            }
            .firstOrNull()

        if (parsedDate != null) {

            val outputFormatter =
                java.text.SimpleDateFormat(
                    "EEE, dd MMM yyyy",
                    java.util.Locale.US
                )

            outputFormatter.format(parsedDate)

        } else {
            date
        }

    } catch (_: Exception) {
        date
    }
}