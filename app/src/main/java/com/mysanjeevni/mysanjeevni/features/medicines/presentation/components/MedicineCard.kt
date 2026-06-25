package com.mysanjeevni.mysanjeevni.features.medicines.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun MedicineCard(
    medicine: Medicine,
) {
    val discount =
        if (medicine.mrp > 0)
            (((medicine.mrp - medicine.price) / medicine.mrp) * 100).toInt()
        else 0

    Column {
        // ── Image Section ──
        MedicineImageSection(
            image = medicine.image,
            discount = discount.toDouble()
        )

        Spacer(Modifier.height(16.dp))

        // ── Tags ──
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MedicineTagChip(medicine.category)
            MedicineTagChip(medicine.productType)
        }

        Spacer(Modifier.height(10.dp))

        // ── Name ──
        AutoText(
            text = medicine.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // ── Quantity ──
        AutoText(
            text = "${medicine.quantity} ${medicine.quantityUnit}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(10.dp))

        // ── Rating + Trusted ──
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFF00C853),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    AutoText(
                        text = "${medicine.rating}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.width(6.dp))
                    AutoText(
                        text = "(${medicine.reviews} Reviews)",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // Vertical divider
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(1.dp)
                    .padding(vertical = 2.dp)
            ) {
                Divider(
                    modifier = Modifier.fillMaxHeight(),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )
            }

            Spacer(Modifier.width(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Color(0xFF00C853),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                AutoText(
                    text = "Trusted Product",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}