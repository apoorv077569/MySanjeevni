package com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun CancelOrderDialog(
    isLoading: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            if (!isLoading) {
                onDismiss()
            }
        },

        title = {
            AutoText(
                text = "Cancel Order"
            )
        },

        text = {
            AutoText(
                text = "Are you sure you want to cancel this order?"
            )
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                AutoText("No")
            }
        },

        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading
            ) {

                if (isLoading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )

                } else {

                    AutoText("Yes, Cancel")
                }
            }
        }
    )
}