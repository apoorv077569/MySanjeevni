package com.mysanjeevni.mysanjeevni.features.medicines.presentation.components

import android.text.Html
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.utils.AutoText

private fun cleanHtml(html: String): String {
    return Html.fromHtml(
        html,
        Html.FROM_HTML_MODE_LEGACY
    )
        .toString()
        .replace(Regex("[ \\t]+"), " ")
        .replace(Regex("\\n{3,}"), "\n\n")
        .trim()
}

@Composable
fun MedicineTabSection(
    description: String,
    specifications: String,
    safetyInformation: String,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        "Description",
        "Ingredients",
        "Benefits"
    )

    // Selected tab content
    val selectedContent = when (selectedTab) {
        0 -> description
        1 -> specifications
        2 -> safetyInformation
        else -> safetyInformation
    }

    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    tabPositions[selectedTab]
                ),
                height = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->

            Tab(
                selected = selectedTab == index,
                onClick = {
                    selectedTab = index
                },
                text = {
                    AutoText(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == index) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        },
                        color = if (selectedTab == index) {
                            Color(0xFF00C853)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            )
        }
    }

    Spacer(
        modifier = Modifier.height(12.dp)
    )

    // Display cleaned HTML content
    AutoText(
        text = cleanHtml(selectedContent),
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = 22.sp
    )

    Spacer(
        modifier = Modifier.height(8.dp)
    )

    TextButton(
        onClick = {}
    ) {
        AutoText(
            text = "Read More  ⌄",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}