package com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.features.prescription.domain.model.Prescription
import com.mysanjeevni.mysanjeevni.utils.AutoText
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun PrescriptionContent(
    prescription: Prescription,
    onDownload: () -> Unit = {}
) {
    val isDarkTheme = isSystemInDarkTheme()

    val cardBackground = if (isDarkTheme) Color(0xFF1A2421) else Color.White
    val textDark = if (isDarkTheme) Color(0xFFF1F5F3) else Color(0xFF071B35)
    val textGrey = if (isDarkTheme) Color(0xFFAABBB5) else Color(0xFF60738A)
    val diagnosisBackground = if (isDarkTheme) Color(0xFF1B2C3A) else Color(0xFFEFF6FF)
    val medicineBackground = if (isDarkTheme) Color(0xFF202825) else Color(0xFFF8F9FA)
    val notesBackground = if (isDarkTheme) Color(0xFF332F1E) else Color(0xFFFFFBEA)
    val statusBackground = if (isDarkTheme) Color(0xFF17382C) else Color(0xFFE7F8EF)
    val green = Color(0xFF00A878)

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            // -----------------------------------------
            // Doctor
            // -----------------------------------------

            AutoText(
                text = "Doctor",
                fontSize = 10.sp,
                color = textGrey
            )

            AutoText(
                text = "Dr. ${prescription.doctorName}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )

            AutoText(
                text = "Reg. No: ${prescription.doctorRegistrationNumber}",
                fontSize = 10.sp,
                color = textGrey
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            // -----------------------------------------
            // Consultation Date
            // -----------------------------------------

            AutoText(
                text = "Consultation Date",
                fontSize = 10.sp,
                color = textGrey
            )

            AutoText(
                text = formatPrescriptionDate(
                    prescription.issueDate
                ),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )

            AutoText(
                text = "Issued: ${
                    formatPrescriptionDate(
                        prescription.issueDate
                    )
                }",
                fontSize = 10.sp,
                color = textGrey
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            // -----------------------------------------
            // Diagnosis
            // -----------------------------------------

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = diagnosisBackground,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 9.dp
                    )
            ) {

                Column {

                    AutoText(
                        text = "Diagnosis",
                        fontSize = 10.sp,
                        color = textDark
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    AutoText(
                        text = prescription.diagnosis,
                        fontSize = 11.sp,
                        color = textDark
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            // -----------------------------------------
            // Medicines
            // -----------------------------------------

            AutoText(
                text = "Medicines:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                prescription.medicines.forEach { medicine ->

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = medicineBackground,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(
                                horizontal = 9.dp,
                                vertical = 7.dp
                            )
                    ) {

                        Column {

                            Row {

                                AutoText(
                                    text = medicine.name,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textDark
                                )

                                AutoText(
                                    text = " - ${medicine.dosage}",
                                    fontSize = 10.sp,
                                    color = textDark
                                )
                            }

                            AutoText(
                                text = "${medicine.frequency} for ${medicine.duration}",
                                fontSize = 9.sp,
                                color = textGrey
                            )
                        }
                    }
                }
            }

            // -----------------------------------------
            // Additional Notes
            // -----------------------------------------

            if (prescription.notes.isNotBlank()) {

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = notesBackground,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(
                            horizontal = 9.dp,
                            vertical = 7.dp
                        )
                ) {

                    Row {

                        AutoText(
                            text = "Additional Notes: ",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )

                        AutoText(
                            text = prescription.notes,
                            fontSize = 9.sp,
                            color = textDark
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            // -----------------------------------------
            // Status + Download
            // -----------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFE7F8EF),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(
                                horizontal = 9.dp,
                                vertical = 4.dp
                            )
                    ) {

                        AutoText(
                            text = prescription.status
                                .replaceFirstChar {
                                    it.uppercase()
                                },
                            fontSize = 9.sp,
                            color = green,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    AutoText(
                        text = "Valid until: ${
                            formatPrescriptionDate(
                                prescription.expiryDate
                            )
                        }",
                        fontSize = 8.sp,
                        color = textGrey
                    )
                }

                Button(
                    onClick = onDownload,
                    modifier = Modifier
                        .height(36.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = green
                    ),
                    contentPadding = ButtonDefaults.ContentPadding
                ) {

                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download PDF",
                        tint = Color.White,
                        modifier = Modifier
                            .height(15.dp)
                            .width(15.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )

                    AutoText(
                        text = "Download PDF",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

private fun formatPrescriptionDate(
    date: String?
): String {

    if (date.isNullOrBlank()) {
        return "-"
    }

    return try {

        val inputFormats = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd"
        )

        var parsedDate: java.util.Date? = null

        for (format in inputFormats) {
            try {
                parsedDate = SimpleDateFormat(
                    format,
                    Locale.US
                ).parse(date)

                if (parsedDate != null) {
                    break
                }
            } catch (_: Exception) {
            }
        }

        if (parsedDate != null) {
            SimpleDateFormat(
                "M/d/yyyy",
                Locale.US
            ).format(parsedDate)
        } else {
            date
        }

    } catch (_: Exception) {
        date
    }
}