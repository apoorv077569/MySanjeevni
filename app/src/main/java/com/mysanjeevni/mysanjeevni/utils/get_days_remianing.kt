package com.mysanjeevni.mysanjeevni.utils

import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun getDaysRemaining(date: String): String {

    return try {

        val formatter =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter.ofPattern("dd MMM yyyy")
            } else {
                TODO("VERSION.SDK_INT < O")
            }

        val collectionDate =
            LocalDate.parse(date, formatter)

        val today = LocalDate.now()

        val days =
            ChronoUnit.DAYS.between(
                today,
                collectionDate
            )

        when {
            days < 0 -> "Expired"
            days == 0L -> "Today"
            days == 1L -> "Tomorrow"
            else -> "In $days days"
        }

    } catch (_: Exception) {
        ""
    }
}