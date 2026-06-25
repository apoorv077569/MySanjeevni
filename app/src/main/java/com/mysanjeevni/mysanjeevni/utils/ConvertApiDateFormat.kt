package com.mysanjeevni.mysanjeevni.utils


    fun convertToApiDateFormat(displayDate: String): String {
        return try {
            val inputFormat = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
            val outputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val date = inputFormat.parse(displayDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            displayDate
        }
    }
