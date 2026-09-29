package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun EmergencyWarningDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var understandChecked by remember { mutableStateOf(false) }
    var termsChecked by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Warning Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFFFCDD2), RoundedCornerShape(8.dp))
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .width(6.dp)
                                .height(150.dp)
                                .background(Color(0xFFD32F2F), RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                        )

                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, null, tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(10.dp))
                                Text("Not for Emergencies", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C))
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = buildAnnotatedString {
                                    append("This service is ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C))) { append("NOT for life-threatening emergencies.") }
                                    append(" If you are experiencing a medical emergency (e.g., chest pain, difficulty breathing, severe bleeding), please ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C))) { append("visit the nearest hospital or call an ambulance immediately.") }
                                },
                                fontSize = 13.sp, color = Color(0xFFB71C1C), lineHeight = 18.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Checkboxes
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Column(Modifier.padding(8.dp)) {
                        CheckboxRow(
                            checked = understandChecked,
                            onCheckedChange = { understandChecked = it },
                            text = buildAnnotatedString {
                                append("I understand and confirm that this is a ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C))) { append("tele-consultation") }
                                append(" and ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C))) { append("NOT for emergency use.") }
                            }
                        )
                        CheckboxRow(
                            checked = termsChecked,
                            onCheckedChange = { termsChecked = it },
                            text = buildAnnotatedString {
                                append("I agree to the ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C))) { append("Teleconsultation Terms of Service.") }
                            }
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = onConfirm,
                    enabled = understandChecked && termsChecked,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A878))
                ) {
                    Text("Continue to Booking", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(Modifier.height(8.dp))

                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun CheckboxRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit, text: androidx.compose.ui.text.AnnotatedString) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange, colors = CheckboxDefaults.colors(checkedColor = Color(0xFFD32F2F)), modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(8.dp))
        Text(text = text, fontSize = 13.sp, color = Color.DarkGray, lineHeight = 18.sp)
    }
}