package com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.utils.AutoText
import java.util.Locale

@Composable
fun MedicineItem(
    medicine: Medicine,
    onAddToCart: (Medicine) -> Unit
) {

    val colorScheme = MaterialTheme.colorScheme
    val mrp = medicine.price + 50
    val discountPercent = String.format(
        Locale.US,
        "%.2f%% OFF",
        ((mrp - medicine.price) / mrp * 100)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {

                if (medicine.image.isNotBlank()) {

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(medicine.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = medicine.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        contentScale = ContentScale.Fit
                    )

                } else {

                    AutoText(
                        text = medicine.icon ?: "💊",
                        fontSize = 36.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                AutoText(
                    text = medicine.brand,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF00A884),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AutoText(
                        text = "₹${medicine.price}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    AutoText(
                        text = "₹$mrp",
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                AutoText(
                    text = discountPercent,
                    color = Color(0xFF4CAF50),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = {
                    onAddToCart(medicine)
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF38D6C6)
                ),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 6.dp
                )
            ) {
                AutoText(
                    text = "ADD",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onPrimary
                )
            }
        }
    }
}
