package com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun FooterTextInfo(isDark: Boolean) {
    Column(modifier = Modifier.padding(16.dp)) {
        AutoText(
            "Our Services",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "MySanjeevni is India's most preferred online healthcare portal that offers end-to-end solutions for many of the pressing health issues faced by Indians today.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
        )
    }
}