package com.mysanjeevni.mysanjeevni.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSlotDropdown(
    selectedTime: String,
    onTimeSelected: (String) -> Unit,
    timeSlots: List<String>,          // 👈 now comes from outside
    enabled: Boolean = true,
    placeholder: String = "Select time slot"
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedTime,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = { Text(placeholder) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false }
        ) {
            if (timeSlots.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No slots available") },
                    onClick = { },
                    enabled = false
                )
            } else {
                timeSlots.forEach { slot ->
                    DropdownMenuItem(
                        text = { Text(slot) },
                        onClick = {
                            onTimeSelected(slot)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}