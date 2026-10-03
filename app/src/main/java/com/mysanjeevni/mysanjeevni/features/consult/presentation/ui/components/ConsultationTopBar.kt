package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun ConsultationsTopBar(
    onBackClick: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    val topBarColor =
        if (isDarkTheme) Color(0xFF162A25)
        else Color(0xFFE9FAF6)

    val circleColor1 =
        if (isDarkTheme) Color(0xFF1D3932)
        else Color(0xFFD4F5EA)

    val circleColor2 =
        if (isDarkTheme) Color(0xFF23483E)
        else Color(0xFFC8F0E2)

    val textColor =
        if (isDarkTheme) Color(0xFFF1F5F3)
        else Color(0xFF071B35)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(112.dp)
            .clip(
                RoundedCornerShape(
                    bottomStart = 28.dp,
                    bottomEnd = 28.dp
                )
            )
            .background(topBarColor)
    ) {

        // Decorative circles
        Box(
            modifier = Modifier
                .size(110.dp)
                .offset(x = 285.dp, y = (-35).dp)
                .background(
                    circleColor1,
                    CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(85.dp)
                .offset(x = 325.dp, y = 55.dp)
                .background(
                    circleColor2,
                    CircleShape
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = if (isDarkTheme) {
                    Color(0xFF23483E)
                } else {
                    Color.White
                },
                shadowElevation = 2.dp
            ) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textColor,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                AutoText(
                    text = "My Consultations",
                    color = textColor,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                AutoText(
                    text = "Manage your appointments",
                    color = if (isDarkTheme) {
                        Color(0xFFAABBB5)
                    } else {
                        Color(0xFF60738A)
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}