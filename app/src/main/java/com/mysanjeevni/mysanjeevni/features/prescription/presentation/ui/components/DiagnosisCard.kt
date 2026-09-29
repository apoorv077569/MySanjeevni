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
import androidx.compose.material.icons.filled.MedicalServices
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
fun DiagnosisCard(
    prescription: Prescription
) {

    if (prescription.diagnosis.isBlank()) {
        return
    }

    InformationCard(
        title = "Diagnosis",
        icon = Icons.Default.MedicalServices,
        value = prescription.diagnosis
    )
}

@Composable
fun InformationCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        PrescriptionGreen.copy(
                            alpha = 0.1f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrescriptionGreen,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column {

                AutoText(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                AutoText(
                    text = value,
                    fontSize = 13.sp,
                    color = TextGrey
                )
            }
        }
    }
}