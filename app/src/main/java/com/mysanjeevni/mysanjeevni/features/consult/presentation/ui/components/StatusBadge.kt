package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.features.consult.presentation.state.ConsultStatus
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun StatusBadge(
    status: ConsultStatus
) {

    val backgroundColor = when (status) {

        ConsultStatus.COMPLETED ->
            LightGreen

        ConsultStatus.SCHEDULED ->
            Color(0xFFE3F2FD)

        ConsultStatus.WAITING ->
            Color(0xFFFFF3E0)

        ConsultStatus.CANCELLED ->
            Color(0xFFFFEBEE)
    }

    val textColor = when (status) {

        ConsultStatus.COMPLETED ->
            Green

        ConsultStatus.SCHEDULED ->
            Color(0xFF1976D2)

        ConsultStatus.WAITING ->
            Color(0xFFF57C00)

        ConsultStatus.CANCELLED ->
            Color(0xFFD32F2F)
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(20.dp)
    ) {

        AutoText(
            text = status.label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
        )
    }
}