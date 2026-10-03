package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mysanjeevni.mysanjeevni.features.currency.presentation.viewmodel.CurrencyViewModel
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTest
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun LabTestPricePanel(
    test: LabTest,
    onViewDetails: () -> Unit,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    val currencyState by currencyViewModel.state.collectAsState()

    // Fallback to INR (rate = 1.0) while loading or if currency fetch failed
    val currencySymbol = currencyState.currencyInfo?.currencySymbol ?: "₹"
    val exchangeRate = currencyState.currencyInfo?.exchangeRate ?: 1.0

    val mrp = test.mrp

    fun formatConverted(inrValue: Int): String {
        val converted = inrValue * exchangeRate
        return "$currencySymbol${
            String.format(
                java.util.Locale.getDefault(),
                if (exchangeRate == 1.0) "%.0f" else "%.2f",
                converted
            )
        }"
    }

    Column(horizontalAlignment = Alignment.End) {
        AutoText(
            text = formatConverted(test.price),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (mrp > test.price) {
            AutoText(
                text = formatConverted(mrp),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = TextDecoration.LineThrough
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onViewDetails,
            modifier = Modifier.width(118.dp).height(42.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Teal)
        ) {
            AutoText(text = "View Details", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary)
            Spacer(Modifier.width(3.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(12.dp))
        }
    }
}
