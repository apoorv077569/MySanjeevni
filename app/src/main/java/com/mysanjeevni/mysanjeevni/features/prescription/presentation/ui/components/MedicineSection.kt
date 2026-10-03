package com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.features.prescription.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.screen.PrescriptionGreen
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.screen.TextDark
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.screen.TextGrey
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun MedicinesSection(
    medicines: List<Medicine>
) {

    if (medicines.isEmpty()) {
        return
    }

    Column {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.MedicalServices,
                contentDescription = null,
                tint = PrescriptionGreen,
                modifier = Modifier.size(20.dp)
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            AutoText(
                text = "Medicines",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        medicines.forEachIndexed { index, medicine ->

            MedicineCard(
                medicine = medicine,
                index = index
            )

            if (index != medicines.lastIndex) {

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }
        }
    }
}


@Composable
private fun MedicineCard(
    medicine: Medicine,
    index: Int
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(15.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            PrescriptionGreen.copy(
                                alpha = 0.1f
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    AutoText(
                        text = "${index + 1}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrescriptionGreen
                    )
                }

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                AutoText(
                    text = medicine.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(
                    vertical = 12.dp
                ),
                color = Color.LightGray.copy(
                    alpha = 0.5f
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                MedicineDetail(
                    title = "Dosage",
                    value = medicine.dosage
                )

                MedicineDetail(
                    title = "Frequency",
                    value = medicine.frequency
                )

                MedicineDetail(
                    title = "Duration",
                    value = medicine.duration
                )
            }
        }
    }
}

@Composable
private fun MedicineDetail(
    title: String,
    value: String
) {

    Column(
        modifier = Modifier.width(90.dp)
    ) {

        AutoText(
            text = title,
            fontSize = 10.sp,
            color = TextGrey
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        AutoText(
            text = value.ifBlank { "—" },
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDark
        )
    }
}

