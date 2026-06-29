package com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun PaymentStatusChip(
    paymentStatus: String,
    modifier: Modifier = Modifier
) {
    val colors = StatusColors.forPaymentStatus(paymentStatus)

    Surface(
        color = colors.container,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        AutoText(
            text = paymentStatus.uppercase(),
            color = colors.content,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}