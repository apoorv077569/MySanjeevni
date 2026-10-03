package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    textColor: Color,
    secondaryText: Color
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = secondaryText,
            modifier = Modifier.size(16.dp)
        )

        Spacer(
            modifier = Modifier.width(7.dp)
        )

        AutoText(
            text = label,
            fontSize = 15.sp,
            color = textColor
        )

        Spacer(
            modifier = Modifier.width(4.dp)
        )

        AutoText(
            text = value,
            fontSize = 15.sp,
            color = secondaryText
        )
    }
}