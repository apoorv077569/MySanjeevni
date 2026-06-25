package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun categoryBg(category: String): Color {
    val dark = androidx.compose.foundation.isSystemInDarkTheme()

    return when (category.lowercase()) {
        "thyroid" ->
            if (dark) Color(0xFF3A2345) else Color(0xFFF3E8FF)

        "diabetes" ->
            if (dark) Color(0xFF4A3415) else Color(0xFFFFF3E0)

        "cardiac" ->
            if (dark) Color(0xFF4B1F24) else Color(0xFFFFEBEE)

        "vitamin" ->
            if (dark) Color(0xFF1E3A52) else Color(0xFFE3F2FD)

        "liver" ->
            if (dark) Color(0xFF4A2437) else Color(0xFFFCE4EC)

        else ->
            if (dark) Color(0xFF123C39) else Color(0xFFE8F5F3)
    }
}

@Composable
fun CategoryIconBox(icon: String, category: String) {
    Box(
        modifier = Modifier
            .size(82.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(categoryBg(category)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = icon, fontSize = 38.sp)
    }
}