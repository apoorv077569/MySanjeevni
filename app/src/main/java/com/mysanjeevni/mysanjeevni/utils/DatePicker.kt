package com.mysanjeevni.mysanjeevni.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                return utcTimeMillis >= calendar.timeInMillis
            }
        }
    )

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { },
        label = { AutoText("Collection Date") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDialog = true }) {
                Icon(androidx.compose.material.icons.Icons.Default.DateRange, contentDescription = "Select date")
            }
        }
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val formatted = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(it)
                        onDateSelected(formatted)
                    }
                    showDialog = false
                }) { AutoText("OK") }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { AutoText("Cancel") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}