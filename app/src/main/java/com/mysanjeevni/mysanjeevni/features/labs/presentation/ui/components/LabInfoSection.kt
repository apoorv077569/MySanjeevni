package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTestDetail

@Composable
fun LabInfoSection(test: LabTestDetail) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoTile(
            modifier = Modifier.weight(1f),
            icon = "⏱",
            iconBg = Color(0xFFE8F5E9),
            label = "Report Time",
            value = test.reportTime
        )
        InfoTile(
            modifier = Modifier.weight(1f),
            icon = "🧪",
            iconBg = Color(0xFFEDE7F6),
            label = "Sample Type",
            value = test.sampleType
        )
        InfoTile(
            modifier = Modifier.weight(1f),
            icon = "🍽",
            iconBg = Color(0xFFFFF3E0),
            label = if (test.fasting) "Fasting Required" else "No Fasting",
            value = if (test.fasting) "${test.fastingHour} hrs" else "0 hrs"
        )
        InfoTile(
            modifier = Modifier.weight(1f),
            icon = "🏠",
            iconBg = Color(0xFFE3F2FD),
            label = "Home Collection",
            value = if (test.homeCollectionAvailable) "Available" else "Visit Lab"
        )
    }
}

@Composable
fun InfoTile(
    modifier: Modifier = Modifier,
    icon: String,
    iconBg: Color,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 18.sp)
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )

        Spacer(Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .height(40.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = value,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}