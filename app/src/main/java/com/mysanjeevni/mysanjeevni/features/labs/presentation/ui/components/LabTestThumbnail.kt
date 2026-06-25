package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun LabTestThumbnail(imageRes: Int, category: String) {
    val bgColor = when {
        category.contains("THYROID", true) ->
            if (isSystemInDarkTheme())
                Color(0xFF3A2345)
            else
                Color(0xFFF3E8FF)

        category.contains("DIABETES", true) ->
            if (isSystemInDarkTheme())
                Color(0xFF4A3415)
            else
                Color(0xFFFFF3E0)

        else ->
            if (isSystemInDarkTheme())
                Color(0xFF123C39)
            else
                Color(0xFFE8F5F3)
    }
    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )
    }
}