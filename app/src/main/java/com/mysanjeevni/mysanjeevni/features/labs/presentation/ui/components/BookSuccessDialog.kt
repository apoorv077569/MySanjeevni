package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
        title = { Text("Booking Confirmed") },
        text = {
            Column {
                Text("Your lab test has been booked successfully.")
                Spacer(Modifier.height(8.dp))
                Text(testName)
                Text(collectionDate)
                Text(collectionTime)
            }
        },
        confirmButton = {
            Button(onClick = onViewBooking) { Text("View Booking") }
        },
        dismissButton = {
            TextButton(onClick = onClose) { Text("Close") }
        }
    )
}