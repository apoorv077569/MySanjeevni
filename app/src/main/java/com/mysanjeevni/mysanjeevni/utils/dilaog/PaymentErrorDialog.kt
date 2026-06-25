package com.mysanjeevni.mysanjeevni.utils.dilaog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun PaymentErrorDialog() {

    val message = PaymentErrorHolder.errorMessage

    if (message != null) {

        AlertDialog(
            onDismissRequest = {
                PaymentErrorHolder.errorMessage = null
            },

            title = {
                Text("Payment Failed")
            },

            text = {
                Text(message.toString())
            },

            confirmButton = {
                Button(
                    onClick = {
                        PaymentErrorHolder.errorMessage = null
                    }
                ) {
                    Text("Try Again")
                }
            }
        )
    }
}