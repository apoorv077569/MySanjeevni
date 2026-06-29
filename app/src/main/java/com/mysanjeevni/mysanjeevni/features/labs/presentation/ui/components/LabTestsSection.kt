package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun LabTestsSection(tests: List<String>) {

    // Show first 3 by default, "View all" expands
    var showAll by remember { mutableStateOf(false) }
    val visibleTests = if (showAll) tests else tests.take(3)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Section header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AutoText(
                text = "Tests Included (${tests.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface            )

            if (tests.size > 3) {
                TextButton(onClick = { showAll = !showAll }) {
                    AutoText(
                        text = if (showAll) "Show less" else "View all",
                        color = Color(0xFF00897B),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF00897B),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Test rows
        visibleTests.forEachIndexed { index, testName ->
            TestRow(
                name = testName,
                index = index
            )
            if (index < visibleTests.lastIndex) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )
            }
        }
    }
}

// Cycle through a few icon emojis based on index
private val testIcons = listOf("🧬", "💉", "🔬", "🧪", "❤️", "🩸", "⚗️", "🫀")

@Composable
fun TestRow(name: String, index: Int) {

    val iconBgColors = listOf(
        Color(0xFFE8F5E9),
        Color(0xFFEDE7F6),
        Color(0xFFFFF3E0),
        Color(0xFFE3F2FD),
        Color(0xFFFCE4EC)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon circle
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconBgColors[index % iconBgColors.size]),
            contentAlignment = Alignment.Center
        ) {
            AutoText(
                text = testIcons[index % testIcons.size],
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        AutoText(
            text = name,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 18.sp
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
    }
}