package com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.PendingOrange
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.SuccessGreen
import com.mysanjeevni.mysanjeevni.utils.AutoText


@Composable
fun StatusChip(text: String, isPositive: Boolean) {
    val bg = if (isPositive) SuccessGreen.copy(alpha = 0.15f) else PendingOrange.copy(alpha = 0.15f)
    val fg = if (isPositive) SuccessGreen else PendingOrange

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = bg
    ) {
        AutoText(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = fg,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}