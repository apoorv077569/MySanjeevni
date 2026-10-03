package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mysanjeevni.mysanjeevni.features.currency.presentation.viewmodel.CurrencyViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import kotlin.math.roundToInt

@Composable
fun LabBottomBar(
    price: Double,
    originalPrice: Double = 0.0,
    testCount: Int,
    onAddToCart: () -> Unit,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    val teal = Color(0xFF00897B)

    val currencyState by currencyViewModel.state.collectAsState()

    // Fallback to INR (rate = 1.0) while loading or if currency fetch failed
    val currencySymbol = currencyState.currencyInfo?.currencySymbol ?: "₹"
    val exchangeRate = currencyState.currencyInfo?.exchangeRate ?: 1.0

    val convertedPrice = price * exchangeRate
    val formattedPrice = "$currencySymbol${
        String.format(
            java.util.Locale.getDefault(),
            if (exchangeRate == 1.0) "%.0f" else "%.2f",
            convertedPrice
        )
    }"

    // Discount % is a ratio, unaffected by currency — keep calculated on raw INR values
    val discountPercent = if (originalPrice > price && originalPrice > 0)
        ((originalPrice - price) / originalPrice * 100).roundToInt()
    else 0

    Surface(
        shadowElevation = 12.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: price + test count + discount
            Column {
                AutoText(
                    text = formattedPrice,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = teal
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AutoText(
                        text = "$testCount Tests",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant                    )

                    if (discountPercent > 0) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFFFD600))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            AutoText(
                                text = "$discountPercent% OFF",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Right: Add to Cart button
            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = teal
                ),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(
                    horizontal = 24.dp,
                    vertical = 14.dp
                ),
                modifier = Modifier.height(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                AutoText(
                    text = "Add To Cart",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }
    }
}