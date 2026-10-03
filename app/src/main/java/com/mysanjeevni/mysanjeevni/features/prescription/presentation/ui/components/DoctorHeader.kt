package com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun DoctorHeader(
    prescription: Prescription
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(
                            PrescriptionGreen.copy(
                                alpha = 0.1f
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = PrescriptionGreen,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(14.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    AutoText(
                        text = if (
                            prescription.doctorName.startsWith(
                                "Dr.",
                                ignoreCase = true
                            )
                        ) {
                            prescription.doctorName
                        } else {
                            "Dr. ${prescription.doctorName}"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    AutoText(
                        text = "Medical Prescription",
                        fontSize = 13.sp,
                        color = TextGrey
                    )

                    if (
                        prescription.doctorRegistrationNumber
                            .isNotBlank()
                    ) {

                        AutoText(
                            text = "Reg. No: ${prescription.doctorRegistrationNumber}",
                            fontSize = 12.sp,
                            color = TextGrey
                        )
                    }
                }
            }

            if (prescription.isVerified) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = PrescriptionGreen,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    AutoText(
                        text = "Verified Prescription",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrescriptionGreen
                    )
                }
            }
        }
    }
}