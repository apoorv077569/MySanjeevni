package com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.utils.AutoText

data class LabTest(val name: String, val subtitle: String, val price: String, val originalPrice: String)

@Composable
fun SectionHeader(title: String, actionText: String = "View All", onAction: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AutoText(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        AutoText(actionText, fontSize = 13.sp, color = AppGreen, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onAction() })
    }
}





