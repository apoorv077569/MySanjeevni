package com.mysanjeevni.mysanjeevni.features.medicines.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun MedicineTabSection(
    description: String,
    specifications: String,
    safetyInformation: String,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf("Description", "Ingredients", "Benefits")

    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                height = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { selectedTab = index },
                text = {
                    AutoText(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == index) Color(0xFF00C853) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }

    Spacer(Modifier.height(12.dp))

    AutoText(
        text = when (selectedTab) {
            0 -> description
            1 -> specifications
            2 -> safetyInformation
            else -> safetyInformation
        },
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = 22.sp
    )

    Spacer(Modifier.height(8.dp))

    TextButton(onClick = {}) {
        AutoText(
            text = "Read More  ⌄",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}