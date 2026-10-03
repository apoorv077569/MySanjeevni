package com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.CalendarToday
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
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.screen.PrescriptionGreen
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.screen.TextDark
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.screen.TextGrey
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun PrescriptionInformation(
    prescription: Prescription
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            AutoText(
                text = "Prescription Details",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                // First row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    DetailItem(
                        icon = Icons.Default.CalendarToday,
                        title = "Consultation Date",
                        value = formatPrescriptionDate(
                            prescription.appointmentDate ?: ""
                        )
                    )

                    DetailItem(
                        icon = Icons.Default.CalendarToday,
                        title = "Issued",
                        value = formatPrescriptionDate(
                            prescription.issueDate
                        )
                    )
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                // Second row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    DetailItem(
                        icon = Icons.Default.CalendarToday,
                        title = "Expires",
                        value = formatPrescriptionDate(
                            prescription.expiryDate
                        )
                    )

                    DetailItem(
                        icon = Icons.AutoMirrored.Filled.ReceiptLong,
                        title = "Consultation",
                        value = prescription.consultationType
                            ?.takeIf { it.isNotBlank() }
                            ?.replaceFirstChar {
                                it.uppercase()
                            }
                            ?: "—"
                    )
                }
            }

        }
    }
}


@Composable
private fun DetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {

    Row(
        modifier = Modifier.width(145.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrescriptionGreen,
            modifier = Modifier.size(18.dp)
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Column {

            AutoText(
                text = title,
                fontSize = 11.sp,
                color = TextGrey
            )

            AutoText(
                text = value.ifBlank { "—" },
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
        }
    }
}