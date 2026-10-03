package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
    val isDarkTheme = isSystemInDarkTheme()
    val dialogBackground = if (isDarkTheme) Color(0xFF1A2421) else Color.White
    val warningBackground = if (isDarkTheme) Color(0xFF351F22) else Color(0xFFFFEBEE)
    val warningBorder = if (isDarkTheme) Color(0xFF6B3035) else Color(0xFFFFCDD2)
    val warningRed = if (isDarkTheme) Color(0xFFFF6B6B) else Color(0xFFD32F2F)
    val warningText = if (isDarkTheme) Color(0xFFFF8A8A) else Color(0xFFB71C1C)
    val normalText = if (isDarkTheme) Color(0xFFE8EFEC) else Color.DarkGray
    val checkboxBorder = if (isDarkTheme) Color(0xFF40504B) else Color(0xFFE0E0E0)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(16.dp),
            color = dialogBackground
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
                        .background(warningBackground, RoundedCornerShape(8.dp))
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
                                Icon(Icons.Default.Warning, null, tint = warningRed, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(10.dp))
                                Text("Not for Emergencies", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = warningText)
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = buildAnnotatedString {
                                    append("This service is ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = warningText)) { append("NOT for life-threatening emergencies.") }
                                    append(" If you are experiencing a medical emergency (e.g., chest pain, difficulty breathing, severe bleeding), please ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = warningText)) { append("visit the nearest hospital or call an ambulance immediately.") }
                                },
                                fontSize = 13.sp, color = warningText, lineHeight = 18.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Checkboxes
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, checkboxBorder)
                ) {
                    Column(Modifier.padding(8.dp)) {
                        CheckboxRow(
                            checked = understandChecked,
                            onCheckedChange = { understandChecked = it },
                            text = buildAnnotatedString {
                                append("I understand and confirm that this is a ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = warningText)) { append("tele-consultation") }
                                append(" and ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = warningText)) { append("NOT for emergency use.") }
                            }
                        )
                        CheckboxRow(
                            checked = termsChecked,
                            onCheckedChange = { termsChecked = it },
                            text = buildAnnotatedString {
                                append("I agree to the ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = warningText)) { append("Teleconsultation Terms of Service.") }
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
                    Text("Cancel", color = if (isDarkTheme) Color(0xFFB8C5C0) else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckboxRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit, text: androidx.compose.ui.text.AnnotatedString) {
    val warningRed = if (isSystemInDarkTheme()) Color(0xFFFF6B6B) else Color(0xFFD32F2F)
    val normalText = if (isSystemInDarkTheme()) Color(0xFFE8EFEC) else Color.DarkGray
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange, colors = CheckboxDefaults.colors(checkedColor = warningRed), modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(8.dp))
        Text(text = text, fontSize = 13.sp, color = normalText, lineHeight = 18.sp)
    }
}