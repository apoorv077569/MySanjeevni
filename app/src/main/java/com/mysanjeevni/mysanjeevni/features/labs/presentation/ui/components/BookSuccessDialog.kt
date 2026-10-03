package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun BookingSuccessDialog(
    testName: String,
    collectionDate: String,
    collectionTime: String,
    onViewBooking: () -> Unit,
    onClose: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { AutoText("Booking Confirmed") },
        text = {
            Column {
                AutoText("Your lab test has been booked successfully.")
                Spacer(Modifier.height(8.dp))
                AutoText(testName)
                AutoText(collectionDate)
                AutoText(collectionTime)
            }
        },
        confirmButton = {
            Button(onClick = onViewBooking) { AutoText("View Booking") }
        },
        dismissButton = {
            TextButton(onClick = onClose) { AutoText("Close") }
        }
    )
}