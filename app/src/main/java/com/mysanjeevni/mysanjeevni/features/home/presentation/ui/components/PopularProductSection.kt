package com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.currency.domain.model.CurrencyInfo
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.utils.AutoText
import java.util.Locale

@Composable
fun PopularProductsSection(
    products: List<Medicine>,
    onProductClick: (Medicine) -> Unit,
    onAddToCart: (Medicine) -> Unit,
    cartViewModel: CartViewModel,
    currencyInfo: CurrencyInfo
) {
    val cardColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface

    if (products.isEmpty()) return

    Column(
        modifier = Modifier
            .height(350.dp)
            .padding(vertical = 8.dp)
    ) {
        // 🏷️ Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AutoText(
                text = "Popular Products",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        // 📦 Horizontal Scrollable List
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(products) { medicine ->
                PopularProductCard(
                    item = medicine,
                    cardColor = cardColor,
                    textColor = textColor,
                    onClick = { onProductClick(medicine) },
                    cartViewModel = cartViewModel,
                    onAddToCart = { onAddToCart(medicine) },
                    currencyInfo = currencyInfo
                )
            }
        }
    }
}

@Composable
fun PopularProductCard(
    item: Medicine,
    cardColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
    cartViewModel: CartViewModel,
    currencyInfo: CurrencyInfo
) {
    val cartState by cartViewModel.state.collectAsState()

    val quantity = cartState.cartItem
        .find { it.id == item.id }
        ?.qty ?: 0

    // Keep original backend INR values for cart
    val cartItem = CartItem(
        id = item.id,
        name = item.name,
        price = item.price,
        originalPrice = item.mrp,
        imageUrl = item.image,
        qty = quantity,
        stock = item.stock,
        requirePrescription = item.requiresPrescription
    )

    val discountPercent = remember(item.price, item.mrp) {
        val price = item.price
        val mrp = item.mrp

        if (mrp > 0 && mrp > price) {
            String.format(
                Locale.getDefault(),
                "%.2f%% OFF",
                ((mrp - price) / mrp * 100)
            )
        } else null
    }

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(350.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp)
        ) {

            Box(
                modifier = Modifier
                    .height(110.dp)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.image.isNotBlank()) {

                        AsyncImage(
                            model = item.image,
                            contentDescription = item.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )

                    } else {

                        AutoText(
                            text = item.icon ?: "💊",
                            fontSize = 32.sp
                        )
                    }
                }

                if (discountPercent != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .background(
                                Color(0xFF43A047),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(
                                horizontal = 4.dp,
                                vertical = 2.dp
                            )
                    ) {
                        AutoText(
                            text = discountPercent,
                            fontSize = 8.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (item.stock <= 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .background(
                                Color.Red,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(
                                horizontal = 4.dp,
                                vertical = 2.dp
                            )
                    ) {
                        AutoText(
                            text = "Out of Stock",
                            fontSize = 7.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // SCROLLABLE CONTENT
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                AutoText(
                    text = item.brand,
                    fontSize = 9.sp,
                    color = Color(0xFF26A69A),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                AutoText(
                    text = item.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                AutoText(
                    text = "${item.quantity} ${item.quantityUnit}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Converted selling price
                    AutoText(
                        text = "${
                            currencyInfo.currencySymbol
                        }${"%.2f".format(
                            item.price * currencyInfo.exchangeRate
                        )}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // Converted MRP
                    AutoText(
                        text = "${
                            currencyInfo.currencySymbol
                        }${"%.2f".format(
                            item.mrp * currencyInfo.exchangeRate
                        )}",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                }

                discountPercent?.let {
                    Spacer(modifier = Modifier.height(2.dp))

                    AutoText(
                        text = it,
                        fontSize = 10.sp,
                        color = Color(0xFF43A047),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // BOTTOM FIXED BUTTON
            if (item.stock <= 0) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(
                            Color.LightGray,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AutoText(
                        text = "Out of Stock",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }

            } else if (quantity == 0) {

                Button(
                    onClick = onAddToCart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF26A69A)
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    AutoText(
                        text = "ADD",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            } else {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(
                            MaterialTheme.colorScheme.surface
                        )
                        .border(
                            1.dp,
                            Color(0xFF26A69A),
                            RoundedCornerShape(8.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable {
                                cartViewModel.decrementQty(cartItem)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = null,
                            tint = Color(0xFF26A69A),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    AutoText(
                        text = quantity.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable {
                                cartViewModel.incrementQty(cartItem)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Color(0xFF26A69A),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}