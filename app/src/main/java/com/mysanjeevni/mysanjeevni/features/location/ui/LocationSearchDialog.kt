package com.mysanjeevni.mysanjeevni.features.location.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun LocationSearchSDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = {onDismiss()},
        confirmButton = {
            Button(onClick = {onConfirm(query)}) {
                AutoText("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = {onDismiss()}) {
                AutoText("Cancel")
            }
        },
        title = {
            AutoText("Enter Delivery Location")
        },
        text = {
            Column {
                TextField(
                    value = query,
                    onValueChange = {query = it},
                    placeholder = {
                        AutoText("Enter city or pincode")
                    }
                )
            }
        }
    )
}