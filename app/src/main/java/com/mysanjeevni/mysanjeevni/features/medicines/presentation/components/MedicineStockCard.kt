package com.mysanjeevni.mysanjeevni.features.medicines.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun MedicineStockCard(stock: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── In Stock / Out of Stock ──
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .then(
                            if (stock > 0)
                                Modifier
                            else
                                Modifier
                        )
                ) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.size(10.dp)) {
                        drawCircle(
                            color = if (stock > 0)
                                androidx.compose.ui.graphics.Color(0xFF00C853)
                            else
                                androidx.compose.ui.graphics.Color(0xFFE53935)
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                AutoText(
                    text = if (stock > 0) "In Stock" else "Out of Stock",
                    color = if (stock > 0) Color(0xFF00C853) else Color(0xFFE53935),
                    fontSize = 14.sp
                )
            }

            Divider(
                modifier = Modifier
                    .height(24.dp)
                    .width(1.dp),
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
            )

            // ── Delivery ──
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalShipping,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                AutoText(
                    text = "Delivery in 2-3 Days",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}