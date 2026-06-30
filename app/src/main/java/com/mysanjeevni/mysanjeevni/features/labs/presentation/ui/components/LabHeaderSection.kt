package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTestDetail
import com.mysanjeevni.mysanjeevni.utils.AutoText
import kotlin.math.roundToInt

@Composable
fun LabHeaderSection(test: LabTestDetail) {

    val tealGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF00897B),
            Color(0xFF26A69A)
        )
    )

    // Discount % calculation
    val discountPercent = if (test.mrp > 0)
        ((test.mrp - test.price) / test.mrp * 100).roundToInt()
    else 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(brush = tealGradient)
            .height(200.dp)
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(16.dp)
                .fillMaxWidth(0.65f)
        ) {

            // Partner badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Color(0xFF00695C).copy(alpha = 0.9f)
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                AutoText(
                    text = "PARTNER TEST",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(Modifier.height(8.dp))

            AutoText(
                text = test.name,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 26.sp
            )

            Spacer(Modifier.height(4.dp))

            AutoText(
                text = "Partner test by ${test.name}",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp
            )

            AutoText(
                text = "Includes ${test.testsIncluded.size} tests • ${if (test.fasting) "Fasting may be required" else "No fasting required"}",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp
            )

            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                AutoText(text = "⭐", fontSize = 14.sp)
                Spacer(Modifier.width(4.dp))
                AutoText(
                    text = "${test.rating}",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Spacer(Modifier.width(12.dp))
                AutoText(
                    text = "| ${test.testsIncluded.size} Tests",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
            }
        }
    }
}