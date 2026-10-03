package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components

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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.features.consult.presentation.state.ConsultItem
import com.mysanjeevni.mysanjeevni.utils.AutoText

private val Green = Color(0xFF00A878)
val LightGreen = Color(0xFFDDF8EA)
val DarkText = Color(0xFF071B35)
val GreyText = Color(0xFF60738A)
val BorderColor = Color(0xFFE2E8EC)

@Composable
fun ConsultCard(
    consult: ConsultItem,
    cardColor: Color,
    textColor: Color,
    secondaryText: Color,
    primaryColor: Color,
    onViewPrescriptionClick: (String) -> Unit,
    onBookAgainClick :(String) -> Unit,
    onViewDetailClick :(String) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            BorderColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // -------------------------------------------------
            // Doctor + Status
            // -------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AutoText(
                    text = consult.doctorName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                StatusBadge(
                    status = consult.status
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // -------------------------------------------------
            // Department
            // -------------------------------------------------

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "•",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Green
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                AutoText(
                    text = consult.specialization,
                    fontSize = 15.sp,
                    color = Green,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // -------------------------------------------------
            // Date
            // -------------------------------------------------

            InfoRow(
                icon = Icons.Default.CalendarToday,
                label = "Date:",
                value = consult.date,
                textColor = textColor,
                secondaryText = secondaryText
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // -------------------------------------------------
            // Consultation Type
            // -------------------------------------------------

            InfoRow(
                icon = if (consult.isVideoCall) {
                    Icons.Default.VideoCall
                } else {
                    Icons.Default.Schedule
                },
                label = "Consultation Type:",
                value = if (consult.isVideoCall) {
                    "Video"
                } else {
                    "In-person"
                },
                textColor = textColor,
                secondaryText = secondaryText
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // -------------------------------------------------
            // Confirmed Time
            // -------------------------------------------------

            if (consult.time.isNotBlank()) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF315BEA),
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(7.dp)
                    )

                    AutoText(
                        text = "Doctor confirmed time:",
                        fontSize = 15.sp,
                        color = Color(0xFF315BEA)
                    )

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    AutoText(
                        text = consult.time,
                        fontSize = 15.sp,
                        color = Color(0xFF315BEA),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // -------------------------------------------------
            // Symptoms
            // -------------------------------------------------

            if (consult.symptoms.isNotBlank()) {

                AutoText(
                    text = "Symptoms: ${consult.symptoms}",
                    fontSize = 15.sp,
                    color = secondaryText
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )
            ConsultationBottomSection(
                consult = consult,
                textColor = textColor,
                secondaryText = secondaryText,
                primaryColor = primaryColor,
                onViewPrescriptionClick = onViewPrescriptionClick,
                onBookAgainClick = onBookAgainClick,
                onViewDetailClick = onViewDetailClick
            )
        }
    }
}