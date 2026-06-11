package com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun MedicineItem(
    medicine: Medicine,
    onAddToCart: (Medicine) -> Unit
) {
    val mrp = medicine.price + 50
    val discountPercent = ((mrp - medicine.price) * 100 / mrp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🖼️ IMAGE
            AsyncImage(
                model = medicine.image,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 📄 DETAILS
            Column(modifier = Modifier.weight(1f)) {

                // ✅ ONLY BRAND (GREEN)
                AutoText(
                    text = medicine.brand,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF00A884), // premium green
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 💰 PRICE ROW
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = "₹${medicine.price}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    AutoText(
                        text = "₹$mrp",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 🟢 DISCOUNT
                AutoText(
                    text = "$discountPercent% OFF",
                    color = Color(0xFF4CAF50),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // 🛒 ADD BUTTON
            Button(
                onClick = { onAddToCart(medicine) },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF38D6C6)
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
            ) {
                AutoText("ADD", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}